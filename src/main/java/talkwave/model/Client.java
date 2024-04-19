package talkwave.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Client {

    PrintStream printStream;
    Socket socket;
    String userId;
    String host;
    Integer port;

    public Client(String userId, String host, Integer port) throws IOException {
        this.userId = userId;
        this.host = host;
        this.port = port;

        this.socket = new Socket(host, port);

        this.printStream = new PrintStream(socket.getOutputStream());
        this.printStream.println(userId);
    }

    public void sendMessage(String message) {
        this.printStream.println(userId + ": " + message);
    }

    public void receiveMessage() throws IOException {
        InputStreamReader inputReader = new InputStreamReader(socket.getInputStream());
        BufferedReader reader = new BufferedReader(inputReader);

        String serverMessage;
        while ((serverMessage = reader.readLine()) != null) {
            String messageOwner = serverMessage.split(":")[0];

            if (!this.isValidUser(messageOwner)) continue;

            if (serverMessage.contains("/sair")) {
                System.out.println(messageOwner + " se desconectou!");
                continue;
            }

            System.out.println(serverMessage);
        }
    }

    public boolean isConnected() {
        return socket.isConnected();
    }

    public void closeConnection() throws IOException {
        this.printStream.close();
        this.socket.close();
    }

    private Boolean isValidUser(String messageOwner) {
        return messageOwner != null && !messageOwner.equals(this.userId);
    }
}
