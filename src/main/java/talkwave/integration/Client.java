package talkwave.integration;

import com.google.gson.Gson;
import talkwave.model.CommandType;
import talkwave.model.ConsoleColors;
import talkwave.model.MessagePrinter;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class Client {

    PrintStream printStream;
    Socket socket;
    String userId;
    String host;
    Integer port;

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
        Message message = new Message(userId, CommandType.EXIT);
        String json = new Gson().toJson(message);
        this.printStream.println(json);
    }

    public void send(Message message) {
        String json = new Gson().toJson(message);
        this.printStream.println(json);
    }

    public void receiveMessage() throws IOException {
        InputStreamReader inputReader = new InputStreamReader(socket.getInputStream());
        BufferedReader reader = new BufferedReader(inputReader);

        String serverMessage;
        while ((serverMessage = reader.readLine()) != null) {
            Message message = new Gson().fromJson(serverMessage, Message.class);

            switch (message.getCommand()) {
                case SEND_MESSAGE -> {
                    MessagePrinter.println(ConsoleColors.BLUE,message.getSender() + ": " + message.getContent());
                }
                case USERS -> {
                    List<String> list = new Gson().fromJson(message.getContent(), ArrayList.class);
                    MessagePrinter.println(ConsoleColors.BLUE,"------------------Usuários------------------");
                    list.forEach(s -> MessagePrinter.println(ConsoleColors.BLUE, s));
                    MessagePrinter.println(ConsoleColors.BLUE,"--------------------------------------------");
                }
                case EXIT -> {
                    MessagePrinter.println(ConsoleColors.RED,message.getSender() + " se desconectou!");
                }
                case BANNED -> {
                    MessagePrinter.println(ConsoleColors.RED,"Você foi banido!");
                    this.closeConnection();
                }
            }
        }
    }

    public void closeConnection() throws IOException {
        this.sendCloseConnectionMessage();
        this.printStream.close();
        this.socket.close();
    }
}
