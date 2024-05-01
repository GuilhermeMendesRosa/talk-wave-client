package talkwave.integration;

import com.google.gson.Gson;
import talkwave.integration.dto.MessageDTO;
import talkwave.ui.ConsoleColors;
import talkwave.ui.MessagePrinter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReceiveMessageRunnable implements Runnable {

	Client client;

	public ReceiveMessageRunnable(Client client) {
		this.client = client;
	}

	@Override
    public void run() {
        try {
            listen(client.getBufferedReader());
        } catch (IOException e) {
            MessagePrinter.println(ConsoleColors.RED, "Bate-papo desconectado. Tente se conectar novamente.");
        }
    }

    private void listen(BufferedReader reader) throws IOException {
        String serverMessage;
        while ((serverMessage = reader.readLine()) != null) {
            MessageDTO message = new Gson().fromJson(serverMessage, MessageDTO.class);

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
                    client.closeConnection();
                }
            }
        }
    }
}