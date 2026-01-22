package org.example.backend.Dao.interfaces;

import java.sql.SQLException;

public interface UserDao<E> extends Dao<E>{
    E getUser(String email, String password) throws SQLException;
}
