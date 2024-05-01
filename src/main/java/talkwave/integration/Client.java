package talkwave.integration;

import com.google.gson.Gson;
import talkwave.integration.dto.MessageDTO;
import talkwave.model.CommandType;

import java.io.*;
import java.net.Socket;

public class Client {

    PrintStream printStream;
    Socket socket;
    final String userId;
    final String host;
    final Integer port;

    public String getUserId() {
        return userId;
    }

    public Client(String userId, String host, Integer port) throws IOException {
        this.userId = userId;
        this.host = host;
        this.port = port;

        this.socket = new Socket(host, port);

        this.printStream = new PrintStream(socket.getOutputStream());
        this.printStream.println(userId);
    }

    public void sendCloseConnectionMessage() {
        MessageDTO message = new MessageDTO(userId, CommandType.EXIT);
        String json = new Gson().toJson(message);
        this.printStream.println(json);
    }

    public void send(MessageDTO message) {
        String json = new Gson().toJson(message);
        this.printStream.println(json);
    }

    public BufferedReader getBufferedReader() throws IOException {
        InputStreamReader inputReader = new InputStreamReader(socket.getInputStream());
        return new BufferedReader(inputReader);
    }

    public void closeConnection() throws IOException {
        this.sendCloseConnectionMessage();
        this.printStream.close();
        this.socket.close();
    }
}
