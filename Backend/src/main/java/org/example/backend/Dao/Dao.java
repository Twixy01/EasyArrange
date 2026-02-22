package org.example.backend.Dao;

import java.util.List;

public interface Dao<E>{
    E findById(long id);
    void create(E object);
    void update(E object);
    void remove(E object);
    List<E> findAll();
}
