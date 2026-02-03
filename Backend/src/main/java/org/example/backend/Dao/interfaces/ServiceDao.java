package org.example.backend.Dao.interfaces;

import java.sql.SQLException;
import java.util.List;

public interface ServiceDao<E> extends Dao<E>{
    //READ
    E findServiceById(long serviceId) throws SQLException;
}
