package com.fourlines.model;


public class ChatMessage {
    public boolean left;
    public String message;
    public String img;

    public ChatMessage(boolean left, String message, String img) {
        super();
        this.left = left;
        this.message = message;
        this.img = img;
    }
}
