module com.example.chatbot {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.chatbot to javafx.fxml;
    exports com.example.chatbot;
}