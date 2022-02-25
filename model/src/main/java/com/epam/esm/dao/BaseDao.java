package com.epam.esm.dao;

import com.epam.esm.entity.Page;
import com.epam.esm.exception.DaoException;

import java.util.List;

public interface BaseDao<T> {
    T find(int id) throws DaoException;

    T save(T t) throws DaoException;

    int delete(int id) throws DaoException;

    List<T> findAll(Page page) throws DaoException;
}
