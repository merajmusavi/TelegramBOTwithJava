package org.example.database;

import org.example.Model.Entity;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.List;
import java.util.Properties;

public class DaoImpl implements DAO {
    private String HOST;
    private String USER;
    private String PASSWORD;

    public DaoImpl() {
        try (InputStream inputStream = new FileInputStream("config.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            HOST = properties.getProperty("host");
            USER = properties.getProperty("user");
            PASSWORD = properties.getProperty("password");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insertUser(List<Entity> entities) {
        try {
            Connection connection = DriverManager.getConnection(HOST, USER, PASSWORD);
            PreparedStatement insertUserToDataBase = connection.prepareStatement("INSERT INTO data(name, username) VALUES(?, ?)");

            insertUserToDataBase.setString(1, entities.get(0).getName());
            insertUserToDataBase.setString(2, entities.get(0).getUsername());

            insertUserToDataBase.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Entity> AllUsers() {
        return null;
    }
}
