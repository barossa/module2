package com.epam.esm.service;

import com.epam.esm.model.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface BaseService<T> {
    Optional<T> find(int id) throws ServiceException;

    List<T> findAll() throws ServiceException;

    Optional<T> save(T t) throws ServiceException;
}
