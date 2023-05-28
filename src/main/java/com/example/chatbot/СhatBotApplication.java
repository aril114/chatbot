package com.example.chatbot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class СhatBotApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(СhatBotApplication.class.getResource("usernameEnter.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("chatbot");
        stage.setScene(scene);
        stage.getIcons().add(new Image("file:icon.png"));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}