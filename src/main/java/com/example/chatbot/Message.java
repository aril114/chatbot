package com.example.chatbot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Сообщение. */
public class Message {
    /** само сообщение, его текст */
    private String message;
    /** время сообщения */
    private LocalDateTime datetime;
    /** имя автора сообщения */
    private String author;

    Message(String aMessage, String anAuthor, LocalDateTime aDateTime) {
        message = aMessage;
        datetime = aDateTime;
        author = anAuthor;
    }

    /** возвращает всю информацию о сообщении в виде строки */
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String datetimeString = datetime.format(formatter);
        String result = datetimeString + " " + author + ": " + message;
        return result;
    }
}
