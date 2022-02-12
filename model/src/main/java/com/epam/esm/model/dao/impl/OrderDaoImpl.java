package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.OrderDao;
import com.epam.esm.model.dto.OrderData;
import com.epam.esm.model.dto.PageData;
import com.epam.esm.model.dto.UserData;
import com.epam.esm.model.exception.DaoException;
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
    private static final String FIND_ALL_JQL = "SELECT o FROM OrderData o";
    private static final String DELETE_ORDER_JQL = "DELETE FROM OrderData o WHERE o.id = :id";

    private final EntityManager entityManager;


    @Override
    public OrderData find(int id) throws DaoException {
        try {
            return entityManager.find(OrderData.class, id);
        } catch (Exception e) {
            throw new DaoException("Can't find order by id", e);
        }
    }

    @Override
    @Transactional
    public OrderData save(OrderData orderData) throws DaoException {
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
    public List<OrderData> findAll(PageData page) throws DaoException {
        try {
            TypedQuery<OrderData> query = entityManager.createQuery(FIND_ALL_JQL, OrderData.class);
            query.setFirstResult(page.getOffset());
            query.setMaxResults(page.getLimit());
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException("Can't find orders", e);
        }
    }

    @Override
    public List<OrderData> findByUser(UserData user, PageData page) throws DaoException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<OrderData> selectQuery = builder.createQuery(OrderData.class);
            Root<OrderData> root = selectQuery.from(OrderData.class);
            selectQuery.where(builder.equal(root.get("user"), user));

            TypedQuery<OrderData> query = entityManager.createQuery(selectQuery);
            query.setFirstResult(page.getOffset());
            query.setMaxResults(page.getLimit());
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException("Can't find order by user", e);
        }
    }
}
