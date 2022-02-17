package com.epam.esm.model.dao;

import com.epam.esm.model.dto.PageData;
import com.epam.esm.model.dto.UserData;
import com.epam.esm.model.exception.DaoException;

public interface UserDao extends BaseDao<UserData> {
    UserData findByName(String name, PageData page) throws DaoException;

    UserData findTopUser() throws DaoException;
}
