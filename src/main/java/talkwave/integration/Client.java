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

    public Client(String userId, String host, Integer port) throws IOException {
        this.userId = userId;
        this.host = host;
        this.port = port;

        this.socket = new Socket(host, port);

        this.printStream = new PrintStream(socket.getOutputStream());
        this.printStream.println(userId);
    }

    public void sendMessage(String messageContent) {
        try {
            Message message = buildTextMessage(messageContent);
            String json = new Gson().toJson(message);
            this.printStream.println(json);
        } catch (Exception e) {
            MessagePrinter.println(ConsoleColors.RED,"Não foi possível enviar a mensagem. Verifique se o comando está correto.");
        }
    }

    private Message buildTextMessage(String messageContent) {
        if (messageContent.contains("to:")) {
            String[] parts = messageContent.split("\\s*to:\\s*|\\s+", 3);

            List<String> recipients = Arrays.asList(parts[1].trim().split(","));
            String content = parts[2].trim();
            return new Message(userId, recipients, content, CommandType.SEND_MESSAGE);
        }

        String[] parts = messageContent.split(" ");
        String recipient = parts[0];
        String content = parts[1];
        return new Message(userId, Collections.singletonList(recipient), content, CommandType.SEND_MESSAGE);
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
        commandLine = commandLine.replace(CommandType.SEND_FILE.getCommandWithPrefix(), "").trim();
        String recipient = commandLine.split(" ")[0];
        Message message = new Message(userId, Collections.singletonList(recipient), CommandType.SEND_FILE);
        String filePath = commandLine.split(" ")[1];

        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        DataOutputStream dataOutputStream = null;
        try {
            // Ler os bytes do arquivo e codificá-los como Base64
            fileInputStream = new FileInputStream(filePath);
            byte[] bytes = new byte[(int) new File(filePath).length()];
            fileInputStream.read(bytes);
            String base64Encoded = Base64.getEncoder().encodeToString(bytes);

            // Construir o objeto JSON com os bytes codificados
            message.setContent(base64Encoded);

            // Obter o fluxo de saída do socket para enviar os dados
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            // Enviar o JSON
            String json = new Gson().toJson(message);
            this.printStream.println(json);

            System.out.println("JSON enviado com sucesso.");
        } catch (IOException e) {
            MessagePrinter.println(ConsoleColors.RED, "Erro ao enviar arquivo");
        } finally {
            try {
                // Fechar os fluxos e o socket
                if (fileInputStream != null) fileInputStream.close();
                if (bufferedInputStream != null) bufferedInputStream.close();
                if (dataOutputStream != null) dataOutputStream.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
