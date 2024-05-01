package talkwave.integration;

import com.google.gson.Gson;
import talkwave.model.ConsoleColors;
import talkwave.model.MessagePrinter;

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
			MessagePrinter.println(ConsoleColors.RED,"Conexão fechada!");
		}
	}

    private void listen(BufferedReader reader) throws IOException {
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
                    client.closeConnection();
                }
            }
        }
    }
}