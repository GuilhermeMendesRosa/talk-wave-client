package talkwave.model;

import java.util.List;

public class Message {

    private String sender;
    private List<String> recipients;
    private String content;
    private CommandType commandType;

    public Message(String sender, CommandType command) {
        this.sender = sender;
        this.commandType = command;
    }

    public Message(String sender, List<String> recipients, CommandType command) {
        this.sender = sender;
        this.recipients = recipients;
        this.commandType = command;
    }

    public Message(String sender, List<String> recipients, String content, CommandType command) {
        this.sender = sender;
        this.recipients = recipients;
        this.content = content;
        this.commandType = command;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }
}
