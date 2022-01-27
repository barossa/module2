package com.epam.esm.model.dao;

import com.epam.esm.model.dto.TagData;
import com.epam.esm.model.exception.DaoException;

import java.util.Set;

public interface TagDao extends BaseDao<TagData> {
    TagData findByName(String name) throws DaoException;

    Set<TagData> findByCertificateId(int id) throws DaoException;

    Set<TagData> saveAll(Set<TagData> tags) throws DaoException;
}
