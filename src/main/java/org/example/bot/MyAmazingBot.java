package org.example.bot;

import org.example.Model.Entity;
import org.example.database.DaoImpl;
import org.example.impl.BackEndIMPL;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.security.SecureRandom;
import java.util.LinkedList;

public class MyAmazingBot extends TelegramLongPollingBot {
    BackEndIMPL backEndIMPL;
    LinkedList<Entity> list;

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
            String chat_id = update.getMessage().getChatId().toString();
            // String string = update.getMessage().getForwardFrom().toString();
            String text = update.getMessage().getText();


            backEndIMPL = new BackEndIMPL(new DaoImpl());
            list = new LinkedList<>();
            message.setChatId(chat_id);
            User user = update.getMessage().getFrom();
            String username =  user.getUserName();
            if (username==null){
                username = user.getId().toString();
            }
            list.add(new Entity(user.getFirstName().toString(),username.toString()));

           boolean is_user_already_exists = backEndIMPL.insertUser(list);
           if (!is_user_already_exists){
               // it means user already started a bot once
               message.setText("hello our old user :)");
           }else {
               // it means its the first time that user starts our bot
               message.setText("hello user! welcome to bot!");
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
