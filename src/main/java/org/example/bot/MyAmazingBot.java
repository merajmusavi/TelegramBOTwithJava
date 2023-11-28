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
import java.util.regex.Pattern;

public class MyAmazingBot extends TelegramLongPollingBot {

    BackEndIMPL backEndIMPL;
    LinkedList<Entity> list;
    private String TOKEN;
    private long ID;
    int status;

    @Override
    public void onUpdateReceived(Update update) {
        backEndIMPL = new BackEndIMPL(new DaoImpl());

        User userup = update.getMessage().getFrom();

        status = backEndIMPL.selectSpecificUserStatus(userup.getUserName());


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


            list = new LinkedList<>();
            message.setChatId(chat_id);
            String username = user.getUserName();


            if (username == null) {
                username = user.getId().toString();
            }
            list.add(new Entity(user.getFirstName().toString(), username.toString()));

            if (text.equals("/panel") && userId == ID) {
                // it will be used in managing status

                backEndIMPL.changestatus(user.getUserName(), 1);
                status = backEndIMPL.selectSpecificUserStatus(user.getUserName());
                message.setText("welcome to the managing panel");


            } else if (status == 1 && text.equals("/users")) {
                List<Entity> entities = backEndIMPL.AllUsers();

                StringBuilder stringBuilder = new StringBuilder();

                for (Entity entity : entities) {
                    stringBuilder.append(entity.getName() + "\n");
                }

                message.setText(stringBuilder.toString());
                backEndIMPL.changestatus(username, 0);
                status = backEndIMPL.selectSpecificUserStatus(user.getUserName());
            } else if (status==3){
                if (patternMatches(text)){
                    backEndIMPL.updateEmail(username,text);
                    backEndIMPL.changestatus(username, 0);
                    message.setText("ایمیلت ثبت شد :)");
                }else {
                    message.setText("ایمیلت فرمت درستی نداره، لطفا درست واردش کن :)");
                }

            }

            else if (text.equals("خروج") && status == 2) {
                backEndIMPL.changestatus(username, 0);
                message.setText("از مرحله ثبت نام خارج شدیم! حالا چیکار کنیم؟!");
            } else if (status == 2) {
                if (text.equals("email")) {
                    message.setText("سلام. ایمیلت رو وارد کن :)");
                    backEndIMPL.changestatus(username, 3);

                } else {
                    message.setText("شما در مرحله ثبت نام هستید، جهت خروج از این مرحله، کلمه خروج را ارسال کنید");
                }
            } else {
                boolean is_user_already_exists = backEndIMPL.insertUser(list);

                if (!is_user_already_exists) {
                    // it means user already started a bot once
                    if (text.equals("/signup")) {
                        message.setText("signup started, enter your email");
                        backEndIMPL.changestatus(username, 2);
                    } else {
                        message.setText("dsfsffse");
                    }


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
    public static boolean patternMatches(String emailAddress) {
       String regexPattern = "^(.+)@(\\S+)$";
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

}
