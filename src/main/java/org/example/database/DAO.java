package org.example.database;

import org.example.Model.Entity;

import java.util.List;

public interface DAO {

    Boolean insertUser(List<Entity> entities);

    List<Entity> AllUsers();

    Boolean changestatus(String username, int status);

    int selectSpecificUserStatus(String username);

    Boolean updateEmail(String username,String email);
}
