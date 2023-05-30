package com.example.chatbot;

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


/** контроллер чата */
public class ChatController {
    private String username; // username хранит ссылку на объект
    @FXML
    private TextArea chatArea; // поле чата
    @FXML
    private TextField userMessageField; // поле для сообщения от пользователя

    private ChatBot chatbot = new ChatBot(); // чатбот

    private String histFile = new String("history.txt"); // файл с историей сообщений

    public void setUsername(String username) {
        this.username = username;
    }

    @FXML
    public void initialize() throws IOException {
        try {
            chatbot.readHistory(histFile);
        }
        catch (NoSuchFileException ignored) {
        }
    }

    @FXML
    protected void onSendButtonClick() throws IOException {
        // извлекается текст сообщения боту
        String input = userMessageField.getText();
        // создается объект сообщения
        Message userMessage = new Message(input, username, LocalDateTime.now());
        // этот объект добавляется в массив сообщений бота
        chatbot.addMessage(userMessage);
        // выводим сообщение пользователя, исключая из сообщения дату
        chatArea.appendText(userMessage.toString().substring(11) + "\n");
        // бот отвечает на сообщение, его ответ записывается в массив сообщений
        MessageWithCode response = chatbot.respond(input);
        Message botMessage = response.getMessage();
        chatbot.addMessage(botMessage);
        // выводится ответ бота
        chatArea.appendText(botMessage.toString().substring(11) + "\n");
        // завершение программы, если пользователь прощается с ботом
        if (response.getMessageCode() == MessageCode.EXIT) {
            save();
            System.exit(0);
        }
    }

    /** записывает историю сообщений в файл */
    protected void save() throws IOException {
        chatbot.writeHistory(histFile);
    }

    /** Срабатывает при нажатии клавиш в поле ввода. Если нажимается интер - отправляет сообщение боту. */
    public void umFieldKeyPressed(KeyEvent keyEvent) throws IOException  {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            onSendButtonClick();
        }
    }
}