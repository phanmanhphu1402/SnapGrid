package com.jackphan.snapgrid.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatResponse {
    @SerializedName("Chats")
    private List<Message> messages;

    public List<Message> getMessages() {
        return messages;
    }
}
