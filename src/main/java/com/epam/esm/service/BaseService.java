package com.epam.esm.service;

import java.util.List;

public interface BaseService<T> {
    T find(int id);

    List<T> findAll();

    T save(T t);

    int delete(int id);
}
