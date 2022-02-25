package com.epam.esm.dao;

import com.epam.esm.entity.Page;
import com.epam.esm.entity.User;
import com.epam.esm.exception.DaoException;
import com.epam.esm.entity.Order;

import java.util.List;

public interface OrderDao extends BaseDao<Order> {
    List<Order> findByUser(User user, Page page) throws DaoException;
}
