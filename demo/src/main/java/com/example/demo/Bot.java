package com.example.demo;//package com.whiskels.telegram.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;

// Аннотация @Component необходима, чтобы наш класс распознавался Spring, как полноправный Bean
@Component
// Наследуемся от TelegramLongPollingBot - абстрактного класса Telegram API
public class Bot extends TelegramLongPollingBot {



    // Аннотация @Value позволяет задавать значение полю путем считывания из application.yaml
    @Value("${bot.name}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;

    Animal base = new Animal();

    /* Перегружаем метод интерфейса LongPollingBot
    Теперь при получении сообщения наш бот будет отвечать сообщением Hi!
     */

    String prevMessage = "";
    String name = "";




    @Override
    public void onUpdateReceived(Update update) {
        try {
            SendMessage sendMessage = new SendMessage();
            Long chatId = update.getMessage().getChatId();
            String message = String.valueOf(update.getMessage().getText());
            String answer = "Извини, я не знаю такое животное(";
            if(message.equals("/start")) {
                sendMessage.setChatId(String.valueOf(chatId));
                sendMessage.setText("Привет! Сперва скажи, как тебя зовут?");
            } else if (prevMessage.equals("/start")) {
                name = message;
                sendMessage.setChatId(String.valueOf(chatId));
                sendMessage.setText("Приятно познакомиться, " + name + "! Давай я расскажу, что я умею. Введи название какого-нибудь животного (с маленькой буквы и без орфорграфических ошибок), и если я его знаю, я расскажу милый и забавный факт о нем.");
            } else {
                for (String key : base.animals.keySet()) {
                    if(message.equals(key)) {
                        answer = base.animals.get(key);
                    }
                }

                sendMessage.setChatId(String.valueOf(chatId));
                sendMessage.setText(answer);
            }
            execute(sendMessage);
            prevMessage = message;
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // Геттеры, которые необходимы для наследования от TelegramLongPollingBot
    public String getBotUsername() {
        return botUsername;
    }

    public String getBotToken() {
        return botToken;
    }
}