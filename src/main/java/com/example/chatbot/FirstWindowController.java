package com.example.chatbot;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class FirstWindowController {
    @FXML
    private TextField usernameField;
    @FXML
    protected void onContinueButtonClick() throws IOException {
        String username = usernameField.getText();
        if (username.equals("")) {
            username = "Пользователь";
        }
        Stage stage = (Stage) usernameField.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(СhatBotApplication.class.getResource("chat.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);

        ChatController controller = fxmlLoader.getController();
        controller.setUsername( username );

        stage.setOnCloseRequest(e -> {
            try {
                controller.save();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
}
