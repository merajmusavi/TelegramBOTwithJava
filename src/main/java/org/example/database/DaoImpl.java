package org.example.database;

import org.example.Model.Entity;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.LinkedList;
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
    public Boolean insertUser(List<Entity> entities) {
        try {
            Connection connection = DriverManager.getConnection(HOST, USER, PASSWORD);
            PreparedStatement insertUserToDataBase = connection.prepareStatement("INSERT IGNORE INTO data(name, username) VALUES(?, ?)");

            insertUserToDataBase.setString(1, entities.get(0).getName());
            insertUserToDataBase.setString(2, entities.get(0).getUsername());

            int i = insertUserToDataBase.executeUpdate();
            if (i == 0) {
                return false;
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public List<Entity> AllUsers() {
        LinkedList<Entity> entities = new LinkedList<Entity>();
        try {
            Connection connection = DriverManager.getConnection(HOST, USER, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM data");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String username = resultSet.getString("username");
                entities.add(new Entity(name, username));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return entities;

    }


    @Override
    public Boolean changestatus(String username, int status) {
        try {
            Connection connection = DriverManager.getConnection(HOST, USER, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE data SET status = ? WHERE username = ? ");
            preparedStatement.setInt(1, status);
            preparedStatement.setString(2, username);

            int i = preparedStatement.executeUpdate();

            if (i > 0) {
                return true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public int selectSpecificUserStatus(String username) {
        int status = 0;
        try {
            Connection connection = DriverManager.getConnection(HOST, USER, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM data WHERE username = ?");

            preparedStatement.setString(1,username);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                 status = resultSet.getInt("status");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return status;
    }

    @Override
    public Boolean updateEmail(String username, String email) {
        try {
            Connection connection = DriverManager.getConnection(HOST, USER, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE data SET email = ? WHERE username = ? ");
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, username);

            int i = preparedStatement.executeUpdate();

            if (i > 0) {
                return true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
