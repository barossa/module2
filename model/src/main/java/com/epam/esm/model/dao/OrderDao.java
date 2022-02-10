package com.epam.esm.model.dao;

import com.epam.esm.model.dto.OrderData;
import com.epam.esm.model.dto.PageData;
import com.epam.esm.model.dto.UserData;
import com.epam.esm.model.exception.DaoException;

import java.util.List;

public interface OrderDao extends BaseDao<OrderData> {
    List<OrderData> findByUser(UserData user, PageData page) throws DaoException;
}
