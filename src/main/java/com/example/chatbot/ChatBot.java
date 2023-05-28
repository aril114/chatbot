package com.example.chatbot;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.regex.*;

// todo: doc
// todo: rename
public class ChatBot {

    private ArrayList<Message> sessionMessages;

    ChatBot() {
         sessionMessages = new ArrayList<Message>(100);
    }

    ChatBot(String fileName) throws IOException {
        sessionMessages = new ArrayList<Message>(100);
        readHistory(fileName);
    }

    public void writeHistory(String fileName) throws IOException {
        var out = new PrintWriter(fileName, StandardCharsets.UTF_8);
        for (Message m : sessionMessages) {
            out.println(m.toString());
        }
        out.close();
    }

    /** sdsds*/
    public void readHistory(String fileName) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Scanner inFile = new Scanner(Path.of(fileName), StandardCharsets.UTF_8);
        while (inFile.hasNext()) {
            String line = inFile.nextLine();
            Pattern pattern = Pattern.compile("(?iuU)(\\d{4}.*-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}) (\\w+): (.*)");
            Matcher matcher = pattern.matcher(line);
            matcher.matches();
            LocalDateTime datetime = LocalDateTime.parse(matcher.group(1), formatter);
            String author = matcher.group(2);
            String messageText = matcher.group(3);
            sessionMessages.add(new Message(messageText, author, datetime));
        }
        inFile.close();
    }

    public void addMessage(Message message) {
        sessionMessages.add(message);
    }

    public static MessageWithCode respond(String input) {
        Message message;
        MessageCode messageCode;

        String answer = mainHandler(input);
        if (answer != null) {
            messageCode = MessageCode.OK;
            message = new Message(answer, "Бот", LocalDateTime.now());
            return new MessageWithCode(message, messageCode);
        }

        answer = exitHandler(input);
        if (answer != null) {
            messageCode = MessageCode.EXIT;
            message = new Message(answer, "Бот", LocalDateTime.now());
            return new MessageWithCode(message, messageCode);
        }

        messageCode = MessageCode.INCORRECT_INPUT;
        message = new Message("Не понимаю.", "Бот", LocalDateTime.now());
        return new MessageWithCode(message, messageCode);
    }

    public static String mainHandler(String input) {
        String output;
        output = greetingHandler(input);
        if (output != null) return output;
        output = mulHandler(input);

        if (output != null) return output;
        output = currentTimeHandler(input);

        return output;
    }

    public static String greetingHandler(String input) {
        return simpleHandler(input, ".*привет.*", "Привет!");
    }

    public static String exitHandler(String input) {
        return simpleHandler(input, ".*(пока|выйти|выход).*", "Пока!");
    }

    public static String currentTimeHandler(String input) {
        String output;
        Pattern pattern = Pattern.compile("(?iuU).*((который час)|(сколько время)|(какое сейчас время)).*|время");
        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            return null;
        }
        LocalTime currentTime = LocalTime.now();
        int hour = currentTime.getHour();
        int minute = currentTime.getMinute();
        int second = currentTime.getSecond();
        output = String.format("%02d:%02d:%02d", hour, minute, second);
        return output;
    }

    public static String mulHandler(String input) {
        String output;
        Pattern pattern = Pattern.compile("(?iuU).*\\bумн\\w*\\b\\s+([+-]?\\d+([.]\\d+)?)\\s+на\\s+([+-]?\\d+([.]\\d+)?)\\.?");
        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            return null;
        }
        float firstNum = Float.parseFloat(matcher.group(1));
        float secondNum = Float.parseFloat(matcher.group(3));
        output = String.format("%f", firstNum * secondNum);
        return output;
    }

    public static String simpleHandler(String input, String aPattern, String output) {
        Pattern pattern = Pattern.compile("(?iuU)" + aPattern);
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            return output;
        }
        else {
            return null;
        }
    }
}

enum MessageCode { OK, EXIT, INCORRECT_INPUT };

class MessageWithCode {
    private Message message;
    private MessageCode messageCode;

    MessageWithCode(Message aMessage, MessageCode aMessageCode) {
        message = aMessage;
        messageCode = aMessageCode;
    }

    public Message getMessage() {
        return message;
    }

    public MessageCode getMessageCode() {
        return messageCode;
    }
}