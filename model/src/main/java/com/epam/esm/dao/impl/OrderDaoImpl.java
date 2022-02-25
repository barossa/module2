package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Page;
import com.epam.esm.entity.User;
import com.epam.esm.exception.DaoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@RequiredArgsConstructor
@Component
public class OrderDaoImpl implements OrderDao {
    private static final String FIND_ALL_JQL = "SELECT o FROM Order o";
    private static final String DELETE_ORDER_JQL = "DELETE FROM Order o WHERE o.id = :id";

    private final EntityManager entityManager;


    @Override
    public Order find(int id) throws DaoException {
        try {
            return entityManager.find(Order.class, id);
        } catch (Exception e) {
            throw new DaoException("Can't find order by id", e);
        }
    }

    @Override
    @Transactional
    public Order save(Order orderData) throws DaoException {
        try {
            entityManager.persist(orderData);
            return orderData;
        } catch (Exception e) {
            throw new DaoException("Can't save order to db", e);
        }
    }

    @Override
    @Transactional
    public int delete(int id) throws DaoException {
        try {
            Query query = entityManager.createQuery(DELETE_ORDER_JQL);
            query.setParameter("id", id);
            return query.executeUpdate();
        } catch (Exception e) {
            throw new DaoException("Can't delete order by id", e);
        }
    }

    @Override
    public List<Order> findAll(Page page) throws DaoException {
        try {
            TypedQuery<Order> query = entityManager.createQuery(FIND_ALL_JQL, Order.class);
            query.setFirstResult(page.getOffset());
            query.setMaxResults(page.getLimit());
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException("Can't find orders", e);
        }
    }

    @Override
    public List<Order> findByUser(User user, Page page) throws DaoException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Order> selectQuery = builder.createQuery(Order.class);
            Root<Order> root = selectQuery.from(Order.class);
            selectQuery.where(builder.equal(root.get("user"), user));

            TypedQuery<Order> query = entityManager.createQuery(selectQuery);
            query.setFirstResult(page.getOffset());
            query.setMaxResults(page.getLimit());
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException("Can't find order by user", e);
        }
    }
}
