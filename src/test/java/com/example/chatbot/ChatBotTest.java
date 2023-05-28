package com.example.chatbot;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChatBotTest {

    @Test
    void respond() {
        MessageWithCode messageWithCode = ChatBot.respond("пока");
        if (messageWithCode.getMessageCode() != MessageCode.EXIT) {
            fail();
        }
        messageWithCode = ChatBot.respond("привет");
        if (messageWithCode.getMessageCode() != MessageCode.OK) {
            fail();
        }
        messageWithCode = ChatBot.respond("АБВ1234567890");
        if (messageWithCode.getMessageCode() != MessageCode.INCORRECT_INPUT) {
            fail();
        }
    }

    @Test
    void mainHandler() {
        String answer;
        answer = ChatBot.mainHandler("Привет.");
        if (answer == null) {
            fail();
        }
        answer = ChatBot.mainHandler("Сколько время?");
        if (answer == null) {
            fail();
        }
        answer = ChatBot.mainHandler("Умножь 5 на 20.");
        if (!answer.matches("100,0+")) {
            fail();
        }
    }

    @Test
    void greetingHandler() {
        String answer = ChatBot.greetingHandler("Привет");
        if (answer == null) {
            fail();
        }
    }

    @Test
    void exitHandler() {
        if (ChatBot.exitHandler("Пока.") == null) {
            fail();
        }
    }

    @Test
    void currentTimeHandler() {
        String answer = ChatBot.currentTimeHandler("Сколько время?");
        if (answer == null) {
            fail();
        }
    }

    @Test
    void mulHandler() {
        String answer = ChatBot.mulHandler("Умножь 5 на 20.");
        if (!answer.matches("100,0+")) {
            fail();
        }
    }

    @Test
    void simpleHandler() {
        String answer = ChatBot.simpleHandler("абв", "абв", "1");
        if (!answer.matches("1")) {
            fail();
        }
    }
}