package talkwave.model;

public class MessageSender {

    private final Client client;

    public MessageSender(Client client) {
        this.client = client;
    }

    public void start() {
        String command;
        do {
            command = SystemScanner.get();
            if (StringValidator.validateCommand(command)) {
                client.sendMessage(command);
            } else {
                System.out.println("Comando inválido.");
            }
        } while (!command.equals("/sair"));
    }
}
