package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class DataBaseTest {
    private String HOST;
    private String USER;
    private String PASSWORD;

    @BeforeEach
    void setUp() {
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

    @Test
    void update_status_of_user(){
        try {
            Connection connection = DriverManager.getConnection(HOST, USER, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE data SET status = ? WHERE username = ? ");
            preparedStatement.setInt(1, 0);
            preparedStatement.setString(2, "Merajmu");

            int i = preparedStatement.executeUpdate();

            if (i > 0) {

            }else {
                Assertions.fail("ffff");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void do_not_insert_if_user_is_already_exists(){
        try {
            Connection connection = DriverManager.getConnection(HOST, USER, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT IGNORE INTO data(name, username) VALUES(?, ?)");
            preparedStatement.setString(1,"ali");
            preparedStatement.setString(2,"al3ii");
            int i = preparedStatement.executeUpdate();
            if (i==0){
                Assertions.fail("username is already exists");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void db_should_be_connected() {

        try {
            Connection connection = DriverManager.getConnection(HOST, USER, PASSWORD);


            if (connection == null) {
                Assertions.fail("do not connected to the database");

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void crud_test(){

        try {
            Connection connection = DriverManager.getConnection(HOST, USER, PASSWORD);

            PreparedStatement insert = connection.prepareStatement("INSERT INTO data(name, username) VALUES(?, ?)");

            PreparedStatement select = connection.prepareStatement("SELECT * FROM data");


            insert.setString(1,"meraj");
            insert.setString(2,"merajmu");

            insert.executeUpdate();


            ResultSet resultSet = select.executeQuery();
            System.out.println(resultSet);

            while (resultSet.next()){
                String name = resultSet.getString("name");

                System.out.println(name);
                assertThat(name).isEqualTo("meraj");


            }



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
