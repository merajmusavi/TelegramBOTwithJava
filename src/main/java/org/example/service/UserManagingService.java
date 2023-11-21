package org.example.service;

import org.example.Model.Entity;

import java.util.List;

public interface UserManagingService {


    void insertUser(List<Entity> entities);

    List<Entity> AllUsers();

}
