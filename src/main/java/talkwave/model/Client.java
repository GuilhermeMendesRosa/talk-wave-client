package talkwave.model;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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

    public void sendMessage(String messageContent) {
        String[] parts = messageContent.split(" ", 2);

        String recipient = parts[0].trim();
        String content = parts[1].trim();
        Message message = new Message(userId, recipient, content, Command.SEND_MESSAGE);

        String json = new Gson().toJson(message);
        this.printStream.println(json);
    }

    public void sendListUsersMessage() {
        Message message = new Message(userId, userId, Command.USERS);

        String json = new Gson().toJson(message);
        this.printStream.println(json);
    }

    public void sendCloseConnectionMessage() {
        Message message = new Message(userId, Command.EXIT);
        String json = new Gson().toJson(message);
        this.printStream.println(json);
    }

    public void sendFile(String commandLine) {
        try {
            String filePath = commandLine.replace(Command.SEND_FILE.getCommandWithPrefix(), "").trim();

            File file = new File(filePath);
            byte[] bytes = new byte[(int) file.length()];

            BufferedInputStream bufferedInputStream = new BufferedInputStream(
                    new FileInputStream(file)
            );

            int readContent;
            while ((readContent = bufferedInputStream.read(bytes)) != -1) {
                this.printStream.write(bytes, 0, readContent);
            }

            this.printStream.flush();
        } catch (IOException e) {
            System.out.println("Erro ao enviar arquivo");
        }
    }

    public void receiveMessage() throws IOException {
        InputStreamReader inputReader = new InputStreamReader(socket.getInputStream());
        BufferedReader reader = new BufferedReader(inputReader);

        String serverMessage;
        while ((serverMessage = reader.readLine()) != null) {
            Message message = new Gson().fromJson(serverMessage, Message.class);

            switch (message.getCommand()) {
                case SEND_MESSAGE -> {
                    System.out.println(message.getSender() + ": " + message.getContent());
                }
                case USERS -> {
                    List<String> list = new Gson().fromJson(message.getContent(), ArrayList.class);
                    System.out.println("------------------Usuários------------------");
                    list.forEach(System.out::println);
                    System.out.println("--------------------------------------------");
                }
                case EXIT -> {
                    System.out.println(message.getSender() + " se desconectou!");
                }
            }
        }
    }

    public boolean isConnected() {
        return socket.isConnected();
    }

    public void closeConnection() throws IOException {
        this.sendCloseConnectionMessage();
        this.printStream.close();
        this.socket.close();
    }

    private Boolean isValidUser(String messageOwner) {
        return messageOwner != null && !messageOwner.equals(this.userId);
    }
}
