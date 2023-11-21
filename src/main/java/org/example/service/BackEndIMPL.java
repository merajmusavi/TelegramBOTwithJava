package org.example.service;

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
    public Boolean insertUser(List<Entity> entities) {
        DAO dao = new DaoImpl();
        return dao.insertUser(entities);
    }

    @Override
    public List<Entity> AllUsers() {
        DAO dao = new DaoImpl();
        return dao.AllUsers();
    }
}