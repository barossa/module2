package com.epam.esm.dao;

import com.epam.esm.entity.Page;
import com.epam.esm.entity.User;
import com.epam.esm.exception.DaoException;

public interface UserDao extends BaseDao<User> {
    User findByName(String name, Page page) throws DaoException;

    User findTopUser() throws DaoException;
}
