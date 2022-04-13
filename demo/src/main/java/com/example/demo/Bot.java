package com.example.demo;//package com.whiskels.telegram.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Scanner;

// Аннотация @Component необходима, чтобы наш класс распознавался Spring, как полноправный Bean
@Component
// Наследуемся от TelegramLongPollingBot - абстрактного класса Telegram API
public class Bot extends TelegramLongPollingBot {



    // Аннотация @Value позволяет задавать значение полю путем считывания из application.yaml
    @Value("${bot.name}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;


    /* Перегружаем метод интерфейса LongPollingBot
    Теперь при получении сообщения наш бот будет отвечать сообщением Hi!
     */

    Scanner sc = new Scanner(System.in);
    int n = 20;
    ArrayList<Integer> num = new ArrayList();



    @Override
    public void onUpdateReceived(Update update) {
        try {
            SendMessage sendMessage1 = new SendMessage();
            SendMessage sendMessage2 = new SendMessage();
            SendMessage sendMessage3 = new SendMessage();
            Long chatId = update.getMessage().getChatId();
            String message = String.valueOf(update.getMessage().getText());
            String answer = "";
            String mass = "";

            if(message.equals("/start")) {
                for (int i = 0; i < n; ++i) {
                    num.add((int) ( Math.random() * 10 ));
                }

                answer = "В этой игре главным действующим лицом является ластик. Стирать придется постоянно, это же война, и неизбежны потери. Многие циферки погибнут ради вашей победы!\n" +
                        "Игра очень быстрая и вариативная, и, в общем, весьма простая.\n" +
                        "Вы получаете ряд цифр от 0 до 9. Например, это может быть ряд 5,3,6,9,0,8,4,6,1,3,2,4,8,7,0,9,5 ? или какой угодно иной.\n" +
                        "Своим ходом игрок может сделать одно из двух возможных в игре действий:\n" +
                        "\n" +
                        "1) изменить в меньшую сторону одну из цифр, максимум до 0 (отрицательных величин в игре нет) ;\n" +
                        "2) стереть любой ноль и все цифры справа него, сократив таким образом длину полосы.\n" +
                        "Проигрывает тот, кто уничтожает последний ноль.\n" +
                        "Чтобы сообщить свой выбор напишите через пробел сперва номер действия, а затем порядковый номер цифры, которую хотите уменьшить, или нуля, после которого хотите все удалить";

                sendMessage1.setChatId(String.valueOf(chatId));
                sendMessage1.setText(answer);

                mass = "";
                for(int i = 0;i<num.size();++i) {
                    mass = mass + num.get(i) + " " ;
                }
                sendMessage2.setChatId(String.valueOf(chatId));
                sendMessage2.setText(mass);
                mass = "";

                execute(sendMessage1);
                execute(sendMessage2);

            } else {

                int type = Integer.parseInt(message.substring(0,1));
                int place = Integer.parseInt(message.substring(2))-1;
                if(type == 1) {
                    num.set(place,num.get(place)-1);
                } else if (type == 2) {
                    for(int i = num.size()-1;i>=place;--i) {
                        num.remove(i);
                    }
                };

                mass = "";
                for(int i = 0;i<num.size();++i) {
                    mass = mass + num.get(i) + " " ;
                }
                sendMessage1.setChatId(String.valueOf(chatId));
                sendMessage1.setText(mass);


                int compType = (int) ( Math.random() * 2 ) +1;
                int compPlace = -1;
                if(type == 2) {
                    for (int i = num.size() - 1; i >= 0; --i) {
                        if (num.get(i) == 0) {
                            compPlace = i;
                        }
                    }
                }
                if(compPlace == -1) {
                    compType = 1;
                    compPlace = (int) ( Math.random() * num.size());
                }
                if(compType == 1) {
                    while(num.get(compPlace)==0) {
                        compPlace = (int) ( Math.random() * num.size());
                    }
                }

                String comp = "Ход компьютера: " + compType + " " + (compPlace+1);
                sendMessage2.setChatId(String.valueOf(chatId));
                sendMessage2.setText(comp);

                System.out.println(compType + " " + (compPlace+1));
                if(compType == 1) {
                    num.set(compPlace,num.get(compPlace)-1);
                } else if (compType == 2) {
                    for(int i = num.size()-1;i>=compPlace;--i) {
                        num.remove(i);
                    }
                }

                mass = "";
                for(int i = 0;i<num.size();++i) {
                    mass = mass + num.get(i) + " " ;
                }
                sendMessage3.setChatId(String.valueOf(chatId));
                sendMessage3.setText(mass);

                execute(sendMessage1);
                execute(sendMessage2);
                execute(sendMessage3);
            }
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