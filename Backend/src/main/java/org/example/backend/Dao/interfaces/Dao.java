package org.example.backend.Dao.interfaces;

import java.sql.SQLException;
import java.util.List;

public interface Dao<E>{
    List<E> findAll() throws SQLException;
}
