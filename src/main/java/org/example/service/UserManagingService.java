package org.example.service;

import org.example.Model.Entity;

import java.util.List;

public interface UserManagingService {


    Boolean insertUser(List<Entity> entities);

    List<Entity> AllUsers();

}
