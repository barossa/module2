package com.epam.esm.dao;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.DaoException;

import java.util.List;
import java.util.Set;

public interface TagDao extends BaseDao<Tag> {
    Tag findByName(String name) throws DaoException;

    Set<Tag> findByCertificate(Certificate certificate) throws DaoException;

    List<Tag> findByNames(List<String> names) throws DaoException;

    Set<Tag> saveAll(Set<Tag> tags) throws DaoException;

    Tag findMostUsedOfUser(User user) throws DaoException;

    Tag findMostUsedOfTopUser() throws DaoException;
}
