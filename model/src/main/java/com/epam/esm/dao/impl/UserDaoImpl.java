package com.epam.esm.dao.impl;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.Page;
import com.epam.esm.entity.User;
import com.epam.esm.exception.DaoException;
import com.epam.esm.dao.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Component
public class UserDaoImpl implements UserDao {
    private static final String FIND_USERS_JQL = "SELECT u FROM User u";
    private static final String FIND_USER_BY_NAME_JQL = "SELECT u FROM User u WHERE u.username = :name";
    private static final String DELETE_USER_JQL = "DELETE FROM User u WHERE u.id = :id";

    private final EntityManager entityManager;

    @Override
    public User find(int id) throws DaoException {
        try {
            return entityManager.find(User.class, id);
        } catch (Exception e) {
            throw new DaoException("Can't find user by id", e);
        }
    }

    @Override
    @Transactional
    public User save(User userData) throws DaoException {
        try {
            entityManager.persist(userData);
            return userData;
        } catch (Exception e) {
            throw new DaoException("Can't save user to db", e);
        }
    }

    @Override
    @Transactional
    public int delete(int id) throws DaoException {
        try {
            Query query = entityManager.createQuery(DELETE_USER_JQL);
            query.setParameter("id", id);
            return query.executeUpdate();
        } catch (Exception e) {
            throw new DaoException("Can't delete user from db", e);
        }
    }

    @Override
    public List<User> findAll(Page page) throws DaoException {
        try {
            TypedQuery<User> query = entityManager.createQuery(FIND_USERS_JQL, User.class);
            query.setFirstResult(page.getOffset());
            query.setMaxResults(page.getLimit());
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException("Can't find users", e);
        }
    }

    @Override
    public User findByName(String name) throws DaoException {
        try {
            TypedQuery<User> query = entityManager.createQuery(FIND_USER_BY_NAME_JQL, User.class);
            query.setParameter("name", name);
            return query.getResultList().stream().findFirst().orElse(null);
        } catch (Exception e) {
            throw new DaoException("Can't find user by name", e);
        }
    }

    @Override
    public User findTopUser() throws DaoException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();

            CriteriaQuery<User> userQuery = builder.createQuery(User.class);
            Root<Order> orderRoot = userQuery.from(Order.class);
            userQuery.select(orderRoot.get("user"));

            Subquery<BigDecimal> sumQuery = userQuery.subquery(BigDecimal.class);
            Root<Order> sumRoot = sumQuery.from(Order.class);
            sumQuery.select(builder.sum(sumRoot.get("cost")))
                    .groupBy(sumRoot.get("user"));

            userQuery.groupBy(orderRoot.get("user"))
                    .having(builder.ge(builder.sum(orderRoot.get("cost")), builder.all(sumQuery)));

            List<User> resultList = entityManager.createQuery(userQuery).getResultList();
            return resultList.stream()
                    .findFirst()
                    .orElse(null);

        } catch (Exception e) {
            throw new DaoException("Can't find top user", e);
        }
    }
}