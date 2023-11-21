package org.example.impl;

import org.example.Model.Entity;
import org.example.database.DAO;
import org.example.database.DaoImpl;
import org.example.service.UserManagingService;

import java.util.List;

public class BackEndIMPL implements UserManagingService {

    final DAO dao;
    // dependency injection
    public BackEndIMPL(DAO dao){
        this.dao = dao;
    }


    @Override
    public void insertUser(List<Entity> entities) {
        DAO dao = new DaoImpl();
        dao.insertUser(entities);
    }

    @Override
    public List<Entity> AllUsers() {
        return null;
    }
}
