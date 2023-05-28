package com.example.chatbot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
    private String message;
    private LocalDateTime datetime;
    private String author;

    Message(String aMessage, String anAuthor, LocalDateTime aDateTime) {
        message = aMessage;
        datetime = aDateTime;
        author = anAuthor;
    }

    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String datetimeString = datetime.format(formatter);
        String result = datetimeString + " " + author + ": " + message;
        return result;
    }
}
