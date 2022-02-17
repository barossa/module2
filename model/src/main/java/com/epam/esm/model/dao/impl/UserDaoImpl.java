package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.UserDao;
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
import javax.persistence.criteria.Subquery;
import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Component
public class UserDaoImpl implements UserDao {
    private static final String FIND_USERS_JQL = "SELECT u FROM UserData u";
    private static final String FIND_USER_BY_NAME_JQL = "SELECT u FROM UserData u WHERE u.username = :name";
    private static final String DELETE_USER_JQL = "DELETE FROM UserData u WHERE u.id = :id";

    private final EntityManager entityManager;

    @Override
    public UserData find(int id) throws DaoException {
        try {
            return entityManager.find(UserData.class, id);
        } catch (Exception e) {
            throw new DaoException("Can't find user by id", e);
        }
    }

    @Override
    @Transactional
    public UserData save(UserData userData) throws DaoException {
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
    public List<UserData> findAll(PageData page) throws DaoException {
        try {
            TypedQuery<UserData> query = entityManager.createQuery(FIND_USERS_JQL, UserData.class);
            query.setFirstResult(page.getOffset());
            query.setMaxResults(page.getLimit());
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException("Can't find users", e);
        }
    }

    @Override
    public UserData findByName(String name, PageData page) throws DaoException {
        try {
            TypedQuery<UserData> query = entityManager.createQuery(FIND_USER_BY_NAME_JQL, UserData.class);
            query.setParameter("name", name);
            query.setFirstResult(page.getOffset());
            query.setMaxResults(page.getLimit());
            return query.getSingleResult();
        } catch (Exception e) {
            throw new DaoException("Can't find user by name", e);
        }
    }

    @Override
    public UserData findTopUser() throws DaoException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();

            CriteriaQuery<UserData> userQuery = builder.createQuery(UserData.class);
            Root<OrderData> orderRoot = userQuery.from(OrderData.class);
            userQuery.select(orderRoot.get("user"));

            Subquery<BigDecimal> sumQuery = userQuery.subquery(BigDecimal.class);
            Root<OrderData> sumRoot = sumQuery.from(OrderData.class);
            sumQuery.select(builder.sum(sumRoot.get("cost")))
                    .groupBy(sumRoot.get("user"));

            userQuery.groupBy(orderRoot.get("user"))
                    .having(builder.ge(builder.sum(orderRoot.get("cost")), builder.all(sumQuery)));

            List<UserData> resultList = entityManager.createQuery(userQuery).getResultList();
            return resultList.stream()
                    .findFirst()
                    .orElse(null);

        } catch (Exception e) {
            throw new DaoException("Can't find top user", e);
        }
    }
}