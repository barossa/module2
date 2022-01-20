package com.epam.esm.model.dao;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.exception.DaoException;

import java.util.List;

public interface TagDao extends BaseDao<Tag> {
    Tag findByName(String name) throws DaoException;

    List<Tag> findByCertificateId(int id) throws DaoException;
}
