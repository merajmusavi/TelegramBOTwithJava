package org.example;

import org.example.Model.Entity;
import org.example.database.DaoImpl;
import org.example.impl.BackEndIMPL;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
            list.add(new Entity(user.getFirstName().toString(),user.getUserName().toString()));

            backEndIMPL.insertUser(list);
            message.setText("inserted");



            try {
                execute(message); // Call method to send the message
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
