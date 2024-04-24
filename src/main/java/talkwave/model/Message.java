package talkwave.model;

public class Message {

    private String sender;
    private String recipient;
    private String content;
    private Command command;

    public Message(String sender, Command command) {
        this.sender = sender;
        this.command = command;
    }

    public Message(String sender, String recipient, Command command) {
        this.sender = sender;
        this.recipient = recipient;
        this.command = command;
    }
    public Message(String sender, String recipient, String content, Command command) {
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.command = command;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }
}
