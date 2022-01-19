package com.epam.esm.model.dao;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.exception.DaoException;

public interface TagDao extends BaseDao<Tag>{
    Tag findByName(String name) throws DaoException;
}
