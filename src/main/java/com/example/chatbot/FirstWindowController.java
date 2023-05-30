package com.example.chatbot;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/** контроллер первого окна, в котором спрашивают имя пользователя */
public class FirstWindowController {
    /** текст на фоне "Введите имя пользователя" */
    @FXML
    private Label label;
    /** поле для имени пользователя */
    @FXML
    private TextField usernameField;
    @FXML
    protected void onContinueButtonClick() throws IOException {
        String username = usernameField.getText();
        if (username.equals("")) {
            username = "Пользователь";
        }
        // вызывается второе окно, окно чата
        Stage stage = (Stage) usernameField.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(СhatBotApplication.class.getResource("chat.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);

            // в контроллере устанавливается имя пользователя
            ChatController controller = fxmlLoader.getController();
            controller.setUsername(username);

            // задается поведение программы при закрытии ее крестиком
            stage.setOnCloseRequest(e -> {
                try {
                    // сохраняется история сообщений
                    controller.save();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        } catch (Exception exception) {
            label.setText("В файле с историей сообщений ошибка.");
        }
    }
}
