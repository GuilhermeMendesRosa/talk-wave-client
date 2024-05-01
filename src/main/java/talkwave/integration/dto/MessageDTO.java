package talkwave.integration.dto;

import talkwave.model.CommandType;

import java.util.List;

public class MessageDTO {

    private String sender;
    private List<String> recipients;
    private String content;
    private CommandType command;
    private FileDTO file;

    public MessageDTO(String sender, CommandType command) {
        this.sender = sender;
        this.command = command;
    }

    public MessageDTO(String sender, List<String> recipients, FileDTO file, CommandType command) {
        this.sender = sender;
        this.recipients = recipients;
        this.file = file;
        this.command = command;
    }

    public MessageDTO(String sender, List<String> recipients, String content, CommandType command) {
        this.sender = sender;
        this.recipients = recipients;
        this.content = content;
        this.command = command;
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

    public CommandType getCommand() {
        return command;
    }

    public void setCommand(CommandType command) {
        this.command = command;
    }
}
