package com.epam.esm.model.dao;

import com.epam.esm.model.exception.DaoException;

import java.util.List;

public interface BaseDao<T> {
    T find(int id) throws DaoException;

    T save(T t) throws DaoException;

    int delete(int id) throws DaoException;

    List<T> findAll() throws DaoException;
}
