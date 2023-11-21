package org.example.database;

import org.example.Model.Entity;

import java.util.List;

public interface DAO {

    void insertUser(List<Entity> entities);

    List<Entity> AllUsers();

}
