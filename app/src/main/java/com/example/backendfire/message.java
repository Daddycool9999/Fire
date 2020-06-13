package com.example.backendfire;

import java.util.Date;

public class message {
    private String messageText;
    private long messageTime;
    private String sender;

    public message(String messageText,String sender) {
        this.messageText = messageText;
        this.sender=sender;

        // Initialize to current time
        messageTime = new Date().getTime();
    }

    public message(){

    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getSender(){return sender;}
    public void setSender(String sender){this.sender=sender;}
    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
