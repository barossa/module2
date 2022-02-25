package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.*;
import com.epam.esm.exception.DaoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TagDaoImpl implements TagDao {
    private static final String FIND_ALL_TAGS_JQL = "SELECT t FROM Tag t";
    private static final String FIND_BY_NAME_JQL = "SELECT t FROM Tag t WHERE t.name = :name";
    private static final String DELETE_TAG_BY_ID_JQL = "DELETE FROM Tag t WHERE t.id = :id";

    private final EntityManager entityManager;

    @Override
    public Tag find(int id) throws DaoException {
        try {
            return entityManager.find(Tag.class, id);
        } catch (Exception e) {
            throw new DaoException("Can't find tag by id", e);
        }
    }

    @Override
    @Transactional
    public Tag save(Tag tag) throws DaoException {
        try {
            entityManager.persist(tag);
            return tag;
        } catch (Exception e) {
            throw new DaoException("Can't save tag to db", e);
        }
    }

    @Override
    @Transactional
    public int delete(int id) throws DaoException {
        try {
            Query query = entityManager.createQuery(DELETE_TAG_BY_ID_JQL);
            query.setParameter("id", id);
            return query.executeUpdate();
        } catch (Exception e) {
            throw new DaoException("Can't delete tag from db", e);
        }
    }

    @Override
    public List<Tag> findAll(Page page) throws DaoException {
        try {
            TypedQuery<Tag> query = entityManager.createQuery(FIND_ALL_TAGS_JQL, Tag.class);
            query.setFirstResult(page.getOffset());
            query.setMaxResults(page.getLimit());
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException("Can't find all tags", e);
        }
    }

    @Override
    public Tag findByName(String name) throws DaoException {
        Tag tag;
        try {
            TypedQuery<Tag> query = entityManager.createQuery(FIND_BY_NAME_JQL, Tag.class);
            query.setParameter("name", name);
            tag = query.getSingleResult();
        } catch (NoResultException e) {
            tag = null;
        } catch (Exception e) {
            throw new DaoException("Can't find tag by name", e);
        }
        return tag;
    }

    @Override
    public Set<Tag> findByCertificate(Certificate certificate) throws DaoException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Tag> selectQuery = builder.createQuery(Tag.class);
            Root<Tag> root = selectQuery.from(Tag.class);
            selectQuery.where(builder.isMember(certificate, root.get("certificates")));

            TypedQuery<Tag> query = entityManager.createQuery(selectQuery);
            return query.getResultStream().collect(Collectors.toSet());

        } catch (Exception e) {
            throw new DaoException("Can't find tag by certificate id", e);
        }
    }

    @Override
    public List<Tag> findByNames(List<String> names) throws DaoException {
        try {
            if (names.isEmpty()) {
                return new ArrayList<>();
            }
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Tag> selectQuery = builder.createQuery(Tag.class);
            Root<Tag> root = selectQuery.from(Tag.class);
            selectQuery.where(root.get("name").in(names));

            TypedQuery<Tag> query = entityManager.createQuery(selectQuery);
            return query.getResultList();

        } catch (Exception e) {
            throw new DaoException("Can't find tags by names", e);
        }
    }

    @Override
    @Transactional
    public Set<Tag> saveAll(Set<Tag> tags) throws DaoException {
        try {
            if (tags.isEmpty()) {
                return new HashSet<>();
            }
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Tag> findExistsQuery = builder.createQuery(Tag.class);
            Root<Tag> root = findExistsQuery.from(Tag.class);
            Predicate condition = createCondition(tags, builder, root);
            findExistsQuery.where(condition);

            List<Tag> existTags = entityManager.createQuery(findExistsQuery).getResultList();
            List<String> existNames = existTags.stream()
                    .map(Tag::getName)
                    .collect(Collectors.toList());

            Set<Tag> savedTags = tags.stream()
                    .filter(tag -> !existNames.contains(tag.getName()))
                    .peek(entityManager::persist)
                    .collect(Collectors.toSet());

            savedTags.addAll(existTags);
            return savedTags;
        } catch (Exception e) {
            throw new DaoException("Can't save all tags to db", e);
        }
    }

    @Override
    public Tag findMostUsedOfUser(User user) throws DaoException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();

            CriteriaQuery<Tag> tagQuery = builder.createQuery(Tag.class);
            Root<Order> orderRoot = tagQuery.from(Order.class);
            Join<Order, Tag> orderTagName = orderRoot.join(Order_.CERTIFICATE)
                    .join(Certificate_.TAGS);

            Subquery<Long> countQuery = tagQuery.subquery(Long.class);
            Root<Order> countRoot = countQuery.from(Order.class);
            Join<Order, Tag> countTagJoin = countRoot.join(Order_.CERTIFICATE)
                    .join(Certificate_.TAGS);

            countQuery.select(builder.count(countTagJoin))
                    .where(builder.equal(countRoot.get("user"), user))
                    .groupBy(countTagJoin);

            tagQuery.select(orderTagName)
                    .where(builder.equal(orderRoot.get("user"), user))
                    .groupBy(orderTagName)
                    .having(builder.ge(builder.count(orderTagName), builder.all(countQuery)));

            List<Tag> resultList = entityManager.createQuery(tagQuery).getResultList();
            return resultList.stream()
                    .findFirst()
                    .orElse(null);

        } catch (Exception e) {
            throw new DaoException("Can't find most used tag of user", e);
        }
    }

    @Override
    public Tag findMostUsedOfTopUser() throws DaoException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();

            CriteriaQuery<Tag> tagQuery = builder.createQuery(Tag.class);
            Root<Order> tagRoot = tagQuery.from(Order.class);
            Join<Order, Tag> orderTagName = tagRoot.join(Order_.CERTIFICATE)
                    .join(Certificate_.TAGS);

            Subquery<User> userQuery = tagQuery.subquery(User.class);
            Root<Order> userRoot = userQuery.from(Order.class);

            Subquery<BigDecimal> sumQuery = userQuery.subquery(BigDecimal.class);
            Root<Order> sumRoot = sumQuery.from(Order.class);
            sumQuery.select(builder.sum(sumRoot.get("cost")))
                    .groupBy(sumRoot.get("user"));

            userQuery.select(userRoot.get("user"))
                    .groupBy(userRoot.get("user"))
                    .having(builder.ge(builder.sum(userRoot.get("cost")), builder.all(sumQuery)));

            Subquery<Long> countQuery = tagQuery.subquery(Long.class);
            Root<Order> countRoot = countQuery.from(Order.class);
            Join<Order, Tag> countTagJoin = countRoot.join(Order_.CERTIFICATE)
                    .join(Certificate_.TAGS);

            countQuery.select(builder.count(countTagJoin))
                    .where(builder.in(countRoot.get("user")).value(userQuery))
                    .groupBy(countTagJoin);

            tagQuery.select(orderTagName)
                    .where(builder.in(tagRoot.get("user")).value(userQuery))
                    .groupBy(orderTagName)
                    .having(builder.ge(builder.count(orderTagName), builder.all(countQuery)));

            List<Tag> resultList = entityManager.createQuery(tagQuery).getResultList();
            return resultList.stream()
                    .findFirst()
                    .orElse(null);

        } catch (Exception e) {
            throw new DaoException("Can't find most used tag of top user", e);
        }

    }

    private Predicate createCondition(Set<Tag> tags,
                                      CriteriaBuilder builder,
                                      Root<Tag> root) throws DaoException {
        return tags.stream()
                .map(Tag::getName)
                .map(name -> builder.equal(root.get("name"), name))
                .reduce(builder::or)
                .orElseThrow(DaoException::new);
    }
}
