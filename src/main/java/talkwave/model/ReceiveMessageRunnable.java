package talkwave.model;

import java.io.IOException;

public class ReceiveMessageRunnable implements Runnable {

	Client client;

	public ReceiveMessageRunnable(Client client) {
		this.client = client;
	}

	@Override
	public void run() {
		try {
			client.receiveMessage();
		} catch (IOException e) {
			MessageBuilder.println(ConsoleColors.RED,"Conexão fechada!");
		}
	}
}