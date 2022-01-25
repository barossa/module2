package com.epam.esm.model.dao;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.exception.DaoException;

import java.util.Set;

public interface TagDao extends BaseDao<Tag> {
    Tag findByName(String name) throws DaoException;

    Set<Tag> findByCertificateId(int id) throws DaoException;

    Set<Tag> saveAll(Set<Tag> tags) throws DaoException;
}
