package org.example.bot;

import org.example.Model.Entity;
import org.example.database.DaoImpl;
import org.example.service.BackEndIMPL;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class MyAmazingBot extends TelegramLongPollingBot {

    BackEndIMPL backEndIMPL;
    LinkedList<Entity> list;
    private String TOKEN;
    private long ID;


    @Override
    public void onUpdateReceived(Update update) {
        try (InputStream inputStream = new FileInputStream("config.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            ID = Long.parseLong(properties.getProperty("id"));
            TOKEN = properties.getProperty("token");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
            String chat_id = update.getMessage().getChatId().toString();
            User user = update.getMessage().getFrom();
            String text = update.getMessage().getText();
            long userId = Long.parseLong(user.getId().toString());


            backEndIMPL = new BackEndIMPL(new DaoImpl());
            list = new LinkedList<>();
            message.setChatId(chat_id);
            String username = user.getUserName();


            if (username == null) {
                username = user.getId().toString();
            }
            list.add(new Entity(user.getFirstName().toString(), username.toString()));

            if (text.equals("/panel") && userId == ID) {
                // it will be used in managing status
                List<Entity> entities = backEndIMPL.AllUsers();

                backEndIMPL.changestatus(user.getUserName(), 1);
                int status = backEndIMPL.selectSpecificUserStatus(user.getUserName());
                while (status==1){
                    if (text.equals("/status")){
                        // status of bot mechanism will be here
                        message.setText("status of bot");
                        break;
                    } else if (text.equals("/remove")) {
                        // remove a specific user will be here
                        message.setText("remove user");
                        break;
                    } else if (text.equals("/ex")) {
                        backEndIMPL.changestatus(user.getUserName(),0);
                        message.setText("khoroj");
                    }
                }

            } else {
                boolean is_user_already_exists = backEndIMPL.insertUser(list);
                if (!is_user_already_exists) {
                    // it means user already started a bot once
                    message.setText(text);

                } else {
                    // it means its the first time that user starts our bot
                }
            }


            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "merajnaaabot";
    }

    @Override
    public String getBotToken() {
        return "6724598515:AAGGHCD-hMy0mWCL0d7GSZhMAD39ASxkUCY";



    }

}
