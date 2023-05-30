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
/** чатбот */
public class ChatBot {
    /** хранимый в чатботе массив сообщений */ 
    private ArrayList<Message> sessionMessages;

    /** конструктор, выделяющий память под сто сообщений */
    ChatBot() {
        sessionMessages = new ArrayList<Message>(100);
    }
    
    /** конструктор, который принимает имя файла с историей сообщений и загружает эту историю */
    ChatBot(String fileName) throws IOException {
        sessionMessages = new ArrayList<Message>(100);
        readHistory(fileName);
    }

    /** запись истории собщений в файл */
    public void writeHistory(String fileName) throws IOException {
        var out = new PrintWriter(fileName, StandardCharsets.UTF_8);
        for (Message m : sessionMessages) {
            out.println(m.toString());
        }
        out.close();
    }

    /** чтение из файла истории сообщений */
    public void readHistory(String fileName) throws IOException, IllegalArgumentException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Scanner inFile = new Scanner(Path.of(fileName), StandardCharsets.UTF_8);
        // регулярное выражение для проверки правильности строки истории
        Pattern pattern = Pattern.compile("(?iuU)(\\d{4}.*-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}) (\\w+): (.*)");
        while (inFile.hasNext()) {
            String line = inFile.nextLine();
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                // извлекаем дату и время из строки, имя автора, текст сообщения
                LocalDateTime datetime = LocalDateTime.parse(matcher.group(1), formatter);
                String author = matcher.group(2);
                String messageText = matcher.group(3);
                // в массив сообщений записывается считанное сообщение
                sessionMessages.add(new Message(messageText, author, datetime));
            }
            else {
                throw new IllegalArgumentException("Неправильная строка в файле: \"" + line +"\"");
            }
        }
        inFile.close();
    }

    /** добавляет сообщение в массив */
    public void addMessage(Message message) {
        sessionMessages.add(message);
    }

    /** Главная функция. Возвращает ответ бота на переданный ему ввод. */
    public static MessageWithCode respond(String input) {
        Message message;
        MessageCode messageCode;

        // проверка основными регулярками
        String answer = mainHandler(input);
        if (answer != null) {
            messageCode = MessageCode.OK;
            message = new Message(answer, "Бот", LocalDateTime.now());
            return new MessageWithCode(message, messageCode);
        }

        // проверка регуляркой на требование выйти
        answer = exitHandler(input);
        if (answer != null) {
            messageCode = MessageCode.EXIT;
            message = new Message(answer, "Бот", LocalDateTime.now());
            return new MessageWithCode(message, messageCode);
        }

        // ни одна из регулярок не совпала с вводом
        messageCode = MessageCode.INCORRECT_INPUT;
        message = new Message("Не понимаю.", "Бот", LocalDateTime.now());
        return new MessageWithCode(message, messageCode);
    }

    /** выполнений основных хэндлеров */
    public static String mainHandler(String input) {
        String output;
        output = greetingHandler(input);
        if (output != null) return output;
        output = mulHandler(input);

        if (output != null) return output;
        output = currentTimeHandler(input);

        return output;
    }

    /** отвечает приветствием на приветствие */
    public static String greetingHandler(String input) {
        return simpleHandler(input, ".*привет.*", "Привет!");
    }

    /** возвращает пока, если получает соответствующий ввод */
    public static String exitHandler(String input) {
        return simpleHandler(input, ".*(пока|выйти|выход).*", "Пока!");
    }

    /** возвращает текущее время */
    public static String currentTimeHandler(String input) {
        String output;
        Pattern pattern = Pattern.compile("(?iuU).*((который час)|(сколько время)|(какое сейчас время)).*|время");
        Matcher matcher = pattern.matcher(input);
        // возвращает null, если нет совпадения
        if (!matcher.matches()) {
            return null;
        }
        // получаем текущее время
        LocalTime currentTime = LocalTime.now();
        // извлекаем из него часы, минуты, секунды
        int hour = currentTime.getHour();
        int minute = currentTime.getMinute();
        int second = currentTime.getSecond();
        output = String.format("%02d:%02d:%02d", hour, minute, second);
        return output;
    }

    /** возвращает произведение двух чисел */
    public static String mulHandler(String input) {
        String output;
        Pattern pattern = Pattern.compile("(?iuU).*\\bумн\\w*\\b\\s+([+-]?\\d+([.]\\d+)?)\\s+на\\s+([+-]?\\d+([.]\\d+)?)\\.?");
        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            return null;
        }
        // первое число
        float firstNum = Float.parseFloat(matcher.group(1));
        // второе число
        float secondNum = Float.parseFloat(matcher.group(3));
        output = String.format("%f", firstNum * secondNum);
        return output;
    }

    /** Простая функция, возвращающая строго заданную не изменяющуюся строку.
        Принимает ввод, регулярку и output. Output выводится, если регулярка совпадает с вводом. */
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

/** Код сообщения */
enum MessageCode { 
    /** Все в порядке. Получен ввод, совпадающий с одним из заданных регулярных выражений. */
    OK,
    /** Пользователь хочет выйти из программы и прощается с чатботом. */
    EXIT,
    /** Ввод не совпадает ни с одним из заданных регулярных выражений. */
    INCORRECT_INPUT };

/** Класс, содержащий сообщение и его код. */
class MessageWithCode {
    /** сообщение */
    private Message message;
    /** код сообщения */
    private MessageCode messageCode;

    /** Конструктор, который принимает сообщение и код. */
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