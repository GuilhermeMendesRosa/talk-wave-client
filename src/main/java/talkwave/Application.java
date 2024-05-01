package talkwave;

import talkwave.integration.Client;
import talkwave.integration.ReceiveMessageRunnable;
import talkwave.model.*;
import talkwave.ui.ConsoleColors;
import talkwave.ui.MessagePrinter;
import talkwave.ui.EnhancedScanner;

import java.io.IOException;

public class Application {

    private static final String host = "localhost";
    private static final Integer port = 8080;

    public static void main(String[] args) {
        try {
            MessagePrinter.println(ConsoleColors.BLUE, """
                    ----------🌊Bem vindo ao TalkWave🌊----------
                    (As mensagens trocadas podem ser auditadas!)
                    """);
            String username = getUserId();
            Client client = new Client(username, host, port);

            new Thread(new ReceiveMessageRunnable(client)).start();

            new CommandListener(client).start();

            client.closeConnection();
        } catch (IOException e) {
            MessagePrinter.println(ConsoleColors.RED,"Erro ao conectar ao servidor");
        }
    }

    private static String getUserId() {
        boolean isValidUsername = false;
        String username = EnhancedScanner.get("Informe o nome do usuário: ");
        do {
            isValidUsername = StringValidator.isNotBlank(username);
            if (!isValidUsername) {
                MessagePrinter.println(ConsoleColors.RED,"Usuário inválido, tente novamente.");
                username = EnhancedScanner.get("Informe o nome do usuário: ");
            }
        } while (!isValidUsername);

        return username;
    }
}
