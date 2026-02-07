package org.example.backend.Dao.interfaces;

import java.sql.SQLException;
import java.util.List;

public interface Dao<E>{
    void create(E object) throws SQLException;
    void update(E object) throws SQLException;
    void remove(E object) throws SQLException;
    List<E> findAll() throws SQLException;
}
