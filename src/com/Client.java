package com;

        import java.io.DataInputStream;
        import java.io.DataOutputStream;
        import java.io.IOException;
        import java.net.Socket;

public class Client {
    private DataInputStream recvStream; //receive form server
    private DataOutputStream sendStream; // send to server

    public Client(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        sendStream = new DataOutputStream(socket.getOutputStream());
        recvStream = new DataInputStream(socket.getInputStream());
    }

    public void sendToServer(int keyInt) throws IOException {
        sendStream.writeInt(keyInt);
    }

    public int recvFromServer() throws IOException {
        return recvStream.readInt();
    }
}
