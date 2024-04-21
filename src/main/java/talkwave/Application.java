package talkwave;

import talkwave.model.*;

import java.io.IOException;

public class Application {

    private static final String host = "localhost";
    private static final Integer port = 8080;

    public static void main(String[] args) {
        try {
            String username = getUserId();
            Client client = new Client(username, host, port);

            new Thread(new ReceiveMessageRunnable(client)).start();

            new CommandListener(client).start();

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
