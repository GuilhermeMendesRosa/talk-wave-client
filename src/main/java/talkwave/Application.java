package talkwave;

import talkwave.model.Client;
import talkwave.model.ReceiveMessageRunnable;
import talkwave.model.StringValidator;
import talkwave.model.SystemScanner;

import java.io.IOException;

public class Application {

    private static final String host = "localhost";
    private static final Integer port = 12345;

    public static void main(String[] args) {
        try {
            String username = getUserId();
            Client client = new Client(username, host, port);

            new Thread(new ReceiveMessageRunnable(client)).start();

            String message;
            do {
                message = SystemScanner.get(null);
                if (StringValidator.isNotBlank(message)) {
                    client.sendMessage(message);
                } else {
                    System.out.println("Comando inválido.");
                }
            } while (!message.equals("/sair"));

            client.closeConnection();
        } catch (IOException e) {
            System.out.println("Erro ao conectar ao servidor");
        }
    }

    private static String getUserId() {
        boolean isValidUsername = false;
        String username = SystemScanner.get("Informe o nome do usuário: ");
        do {
            isValidUsername = StringValidator.isNotBlank(username);
            if (!isValidUsername) {
                System.out.println("Usuário inválido, tente novamente.");
                username = SystemScanner.get("Informe o nome do usuário: ");
            }
        } while (!isValidUsername);

        return username;
    }
}
