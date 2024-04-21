package talkwave.model;

import com.google.gson.Gson;

import java.io.*;
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

    public void sendMessage(String messageContent) {
        Message message = new Message(userId, "joao", messageContent);

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
            String messageOwner = serverMessage.split(":")[0];

            if (!this.isValidUser(messageOwner)) continue;

            if (serverMessage.contains(Command.EXIT.getCommandWithPrefix())) {
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
