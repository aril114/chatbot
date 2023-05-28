package com.example.chatbot;

// todo: author

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDateTime;
import java.util.ArrayList;


// todo: sparate classes

public class ChatController {
    private String username; // username хранит ссылку на объект
    @FXML
    private TextArea chatArea;
    @FXML
    private TextField userMessageField;

    private ChatBot chatbot = new ChatBot();

    private String histFile = new String("history.txt");

    public void setUsername(String username) {
        this.username = username;
    }

    @FXML
    public void initialize() throws IOException {
        try {
            chatbot.readHistory("history.txt");
        }
        catch (NoSuchFileException ignored) {
        }
    }

    @FXML
    protected void onSendButtonClick() throws IOException {
        String input = userMessageField.getText();
        Message userMessage = new Message(input, username, LocalDateTime.now());
        chatbot.addMessage(userMessage);
        // выводим сообщение пользователя, исключая из сообщения дату
        chatArea.appendText(userMessage.toString().substring(11) + "\n");

        MessageWithCode response = chatbot.respond(input);
        Message botMessage = response.getMessage();
        chatbot.addMessage(botMessage);
        chatArea.appendText(botMessage.toString().substring(11) + "\n");
        if (response.getMessageCode() == MessageCode.EXIT) {
            save();
            System.exit(0);
        }
    }

    protected void save() throws IOException {
        chatbot.writeHistory(histFile);
    }

    public void umFieldKeyPressed(KeyEvent keyEvent) throws IOException  {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            onSendButtonClick();
        }
    }
}