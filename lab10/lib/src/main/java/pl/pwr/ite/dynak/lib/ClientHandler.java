package pl.pwr.ite.dynak.lib;

import pl.pwr.ite.dynak.lib.utils.MessageListener;
import pl.pwr.ite.dynak.lib.utils.PacketType;

import javax.net.ssl.SSLSocket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ClientHandler implements Runnable {

    private final SSLSocket socket;
    private final TlsServer server;
    private final MessageListener listener;
    private DataInputStream dataInput;
    private DataOutputStream dataOutput;

    public ClientHandler(SSLSocket socket, TlsServer server, MessageListener listener) throws IOException {
        this.socket = socket;
        this.server = server;
        this.listener = listener;

        dataInput = new DataInputStream(socket.getInputStream());
        dataOutput = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            while (true) {
                int type = dataInput.readInt();

                if (type == PacketType.MESSAGE.ordinal()) {
                    String message = dataInput.readUTF();

                    server.broadcastMessage(message);

                } else if (type == PacketType.ATTACHMENT.ordinal()) {
                    String fileName = dataInput.readUTF();
                    long fileSize = dataInput.readLong();

                    byte[] fileBytes = dataInput.readNBytes((int) fileSize);

                    listener.onLog("Received attachment from client: " + fileName);
                    server.broadcastFile(fileName, fileBytes);

                } else {
                    listener.onLog("Unknown packet type received: " + type);
                }
            }
        } catch (Exception e) {
            listener.onLog("Client disconnected");

        } finally {
            server.clients.remove(this);

            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        try {
            dataOutput.writeInt(PacketType.MESSAGE.ordinal());
            dataOutput.writeUTF(message);
            dataOutput.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendFile(String fileName, byte[] fileBytes) {
        try {
            dataOutput.writeInt(PacketType.ATTACHMENT.ordinal());
            dataOutput.writeUTF(fileName);
            dataOutput.writeLong(fileBytes.length);
            dataOutput.write(fileBytes);
            dataOutput.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
