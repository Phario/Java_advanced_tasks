package pl.pwr.ite.dynak.lib;

import pl.pwr.ite.dynak.lib.utils.MessageListener;
import pl.pwr.ite.dynak.lib.utils.PacketType;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import static pl.pwr.ite.dynak.lib.utils.Passwords.KEYSTORE_PASSWORD;
import static pl.pwr.ite.dynak.lib.utils.Passwords.TRUSTSTORE_PASSWORD;

public class TlsServer {
    protected final List<ClientHandler> clients = new ArrayList<>();
    private final MessageListener listener;

    public TlsServer(MessageListener listener) {
        this.listener = listener;
    }

    public void start(int port) throws Exception {


        // SERVER KEYSTORE
        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        try (FileInputStream fis =
                     new FileInputStream("server.p12")) {

            keyStore.load(fis, KEYSTORE_PASSWORD.toCharArray());
        }

        KeyManagerFactory kmf =
                KeyManagerFactory.getInstance(
                        KeyManagerFactory.getDefaultAlgorithm());

        kmf.init(keyStore, KEYSTORE_PASSWORD.toCharArray());

        // TRUSTSTORE
        KeyStore trustStore = KeyStore.getInstance("PKCS12");

        try (FileInputStream fis =
                     new FileInputStream("server-truststore.p12")) {

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

        SSLServerSocketFactory factory =
                sslContext.getServerSocketFactory();

        SSLServerSocket serverSocket =
                (SSLServerSocket) factory.createServerSocket(port);

        serverSocket.setNeedClientAuth(true);

        listener.onLog("TLS Server started on " + port);

        while (true) {
            SSLSocket socket =
                    (SSLSocket) serverSocket.accept();

            listener.onLog("Client connected");

            ClientHandler client =
                    new ClientHandler(socket, this, listener);

            clients.add(client);

            new Thread(client).start();
        }
    }

    protected void broadcastMessage(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
            listener.onLog("Sending message to client: " + message);
        }
    }

    protected void broadcastFile(String fileName, byte[] fileBytes) {
        for (ClientHandler client : clients) {
            client.sendFile(fileName, fileBytes);
            listener.onLog("Sending attachment to client: " + fileName);
        }
    }
}
