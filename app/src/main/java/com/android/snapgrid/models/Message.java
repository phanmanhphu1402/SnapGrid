package com.android.snapgrid.models;

public class Message {
    private int idMessage;
    private int idSender;
    private int idRecipient;
    private String content;
    private String timeSent;

    public Message() {
    }

    public Message(int idMessage, int idSender, int idRecipient, String content, String timeSent) {
        this.idMessage = idMessage;
        this.idSender = idSender;
        this.idRecipient = idRecipient;
        this.content = content;
        this.timeSent = timeSent;
    }

    public int getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(int idMessage) {
        this.idMessage = idMessage;
    }

    public int getIdSender() {
        return idSender;
    }

    public void setIdSender(int idSender) {
        this.idSender = idSender;
    }

    public int getIdRecipient() {
        return idRecipient;
    }

    public void setIdRecipient(int idRecipient) {
        this.idRecipient = idRecipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }
}
