package talkwave.model;

import talkwave.integration.Client;
import talkwave.integration.dto.MessageDTO;
import talkwave.integration.MessageBuilder;
import talkwave.ui.ConsoleColors;
import talkwave.ui.MessagePrinter;
import talkwave.ui.EnhancedScanner;

import java.util.List;

public class CommandListener {

    private final Client client;

    public CommandListener(Client client) {
        this.client = client;
    }

    public void start() {
        while (true) {
            try {
                String commandLine = EnhancedScanner.get();
                MessageDTO message = new MessageBuilder(client.getUserId(), commandLine).build();
                if (message != null) client.send(message);
            } catch (InvalidCommandException invalidCommandException) {
                onInvalidCommand();
            } catch (Exception e) {
                MessagePrinter.println(ConsoleColors.RED,"Erro ao processar comando. Tente novamente.");
            }
        }
    }

    private void onInvalidCommand() {
        MessagePrinter.println(ConsoleColors.RED,"Comando inválido. Tente novamente.");
        MessagePrinter.println(ConsoleColors.BLUE,"Comandos disponíveis: ");

        getCommandExamples().forEach(command -> MessagePrinter.println(ConsoleColors.BLUE, command));
    }

    private List<String> getCommandExamples() {
        return List.of(
            "/send message <usuário> <mensagem>",
            "/send file <usuário> <caminho do arquivo>",
            "/users",
            "/exit"
        );
    }
}
