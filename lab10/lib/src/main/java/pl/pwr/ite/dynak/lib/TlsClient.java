package pl.pwr.ite.dynak.lib;

import pl.pwr.ite.dynak.lib.utils.MessageListener;
import pl.pwr.ite.dynak.lib.utils.PacketType;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;

import static pl.pwr.ite.dynak.lib.utils.Passwords.KEYSTORE_PASSWORD;
import static pl.pwr.ite.dynak.lib.utils.Passwords.TRUSTSTORE_PASSWORD;

public class TlsClient {
    private SSLSocket socket;
    DataInputStream input;
    DataOutputStream output;

    private final MessageListener listener;

    public TlsClient(MessageListener listener) {
        this.listener = listener;
    }

    public void connect(String host, int port) throws Exception {
        // CLIENT KEYSTORE
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream fis = new FileInputStream("client.p12")) {
            keyStore.load(fis, KEYSTORE_PASSWORD.toCharArray());
        }

        KeyManagerFactory kmf =
                KeyManagerFactory.getInstance(
                        KeyManagerFactory.getDefaultAlgorithm());

        kmf.init(keyStore, KEYSTORE_PASSWORD.toCharArray());

        // TRUSTSTORE
        KeyStore trustStore = KeyStore.getInstance("PKCS12");

        try (FileInputStream fis =
                     new FileInputStream("client-truststore.p12")) {

            trustStore.load(fis, TRUSTSTORE_PASSWORD.toCharArray());
        }

        TrustManagerFactory tmf =
                TrustManagerFactory.getInstance(
                        TrustManagerFactory.getDefaultAlgorithm());

        tmf.init(trustStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");

        sslContext.init(
                kmf.getKeyManagers(),
                tmf.getTrustManagers(),
                null
        );

        SSLSocketFactory factory = sslContext.getSocketFactory();

        socket = (SSLSocket) factory.createSocket(host, port);

        socket.startHandshake();

        input = new DataInputStream(socket.getInputStream());

        output = new DataOutputStream(socket.getOutputStream());

        listener.onLog("Connected to server");

        startListening();
    }

    private void startListening() {
        Thread thread = new Thread(() -> {

            try {
                while (true) {
                    int type = input.readInt();

                    if (type == PacketType.MESSAGE.ordinal()) {
                        String msg = input.readUTF();
                        listener.onLog("Client received message: " + msg);
                        listener.onMessage(msg);
                    } else if (type == PacketType.ATTACHMENT.ordinal()) {
                        receiveFile();
                    } else {
                        listener.onLog("Unknown packet type received: " + type);
                    }
                }

            } catch (Exception e) {
                listener.onLog("Connection closed");
            }

        });

        thread.setDaemon(true);
        thread.start();
    }

    public void sendMessage(String message) throws IOException {
        output.writeInt(PacketType.MESSAGE.ordinal());

        output.writeUTF(message);

        output.flush();
    }

    public void sendFile(File file) throws IOException {

        output.writeInt(PacketType.ATTACHMENT.ordinal());

        output.writeUTF(file.getName());

        output.writeLong(file.length());

        try (FileInputStream fis = new FileInputStream(file)) {

            byte[] buffer = new byte[8192];

            int read;

            while ((read = fis.read(buffer)) != -1) {

                output.write(buffer, 0, read);
            }
        }

        output.flush();

        listener.onLog("File sent: " + file.getName());
    }

    private void receiveFile() throws IOException {

        String fileName = input.readUTF();

        long fileSize = input.readLong();

        File downloadsDir = new File("downloads");

        if (!downloadsDir.exists()) {
            downloadsDir.mkdirs();
        }

        File outputFile = new File(downloadsDir, fileName);

        try (FileOutputStream fos =
                     new FileOutputStream(outputFile)) {

            byte[] buffer = new byte[8192];

            long remaining = fileSize;

            while (remaining > 0) {

                int read = input.read(
                        buffer,
                        0,
                        (int)Math.min(buffer.length, remaining)
                );

                if (read == -1) {
                    break;
                }

                fos.write(buffer, 0, read);

                remaining -= read;
            }
        }

        listener.onLog("Received file: " + fileName);

        listener.onFileReceived(outputFile);
    }

    public void disconnect() throws IOException {
        if (socket != null) {
            socket.close();
        }
    }
}
