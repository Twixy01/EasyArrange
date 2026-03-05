package org.example.backend.Repository;

import java.util.List;

public interface Dao<E>{
    E findById(long id);
    boolean create(E object);
    boolean update(E object);
    boolean remove(E object);
    List<E> findAll();
}
