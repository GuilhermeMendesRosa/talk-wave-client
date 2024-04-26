package talkwave.integration;

import com.google.gson.Gson;
import talkwave.model.CommandType;
import talkwave.model.ConsoleColors;
import talkwave.model.MessagePrinter;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        boolean hasRecipients = messageContent.contains("to:");
        if (hasRecipients) {
            sendMessageWUsers(messageContent);
        } else {
            sendMessageWNoUsers(messageContent);
        }
    }

    private void sendMessageWUsers(String messageContent) {
        try {
            String[] parts = messageContent.split("\\s*to:\\s*|\\s+", 3);

            List<String> recipients = Arrays.asList(parts[1].trim().split(","));
            String content = parts[2].trim();
            Message message = new Message(userId, recipients, content, CommandType.SEND_MESSAGE);

            String json = new Gson().toJson(message);
            this.printStream.println(json);
        } catch (Exception e) {
            MessagePrinter.println(ConsoleColors.RED,"Não foi possível enviar a mensagem");
        }
    }

    private void sendMessageWNoUsers(String messageContent) {
        Message message = new Message(userId, null, messageContent, CommandType.SEND_MESSAGE);
        String json = new Gson().toJson(message);
        this.printStream.println(json);
    }

    public void sendListUsersMessage() {
        Message message = new Message(userId, Collections.singletonList(userId), CommandType.USERS);

        String json = new Gson().toJson(message);
        this.printStream.println(json);
    }

    public void sendCloseConnectionMessage() {
        Message message = new Message(userId, CommandType.EXIT);
        String json = new Gson().toJson(message);
        this.printStream.println(json);
    }

    public void sendFile(String commandLine) {
        try {
            String filePath = commandLine.replace(CommandType.SEND_FILE.getCommandWithPrefix(), "").trim();

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
            MessagePrinter.println(ConsoleColors.RED, "Erro ao enviar arquivo");
        }
    }

    public void receiveMessage() throws IOException {
        InputStreamReader inputReader = new InputStreamReader(socket.getInputStream());
        BufferedReader reader = new BufferedReader(inputReader);

        String serverMessage;
        while ((serverMessage = reader.readLine()) != null) {
            Message message = new Gson().fromJson(serverMessage, Message.class);

            switch (message.getCommandType()) {
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
