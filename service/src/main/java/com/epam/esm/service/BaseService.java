package com.epam.esm.service;

import com.epam.esm.dto.PageDto;

import java.util.List;

public interface BaseService<T> {
    T find(int id);

    List<T> findAll(PageDto page);

    T save(T t);

    T delete(int id);
}
