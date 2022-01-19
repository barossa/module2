package com.epam.esm.model.dao;

import com.epam.esm.model.exception.DaoException;

public interface BaseDao<T> {
    T find(int id) throws DaoException;
    T save(T t) throws DaoException;
    int delete(T t) throws DaoException;
}
