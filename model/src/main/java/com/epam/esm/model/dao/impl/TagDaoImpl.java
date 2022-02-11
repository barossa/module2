package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.dto.CertificateData;
import com.epam.esm.model.dto.PageData;
import com.epam.esm.model.dto.TagData;
import com.epam.esm.model.exception.DaoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TagDaoImpl implements TagDao {
    private static final String FIND_ALL_TAGS_JQL = "SELECT t FROM TagData t";
    private static final String FIND_BY_NAME_JQL = "SELECT t FROM TagData t WHERE t.name = :name";
    private static final String DELETE_TAG_BY_ID_JQL = "DELETE FROM TagData t WHERE t.id = :id";

    private final EntityManager entityManager;

    @Override
    public TagData find(int id) throws DaoException {
        try {
            return entityManager.find(TagData.class, id);
        } catch (Exception e) {
            throw new DaoException("Can't find tag by id", e);
        }
    }

    @Override
    public TagData save(TagData tagData) throws DaoException {
        try {
            entityManager.persist(tagData);
            return tagData;
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
    public List<TagData> findAll(PageData page) throws DaoException {
        try {
            TypedQuery<TagData> query = entityManager.createQuery(FIND_ALL_TAGS_JQL, TagData.class);
            query.setFirstResult(page.getOffset());
            query.setMaxResults(page.getLimit());
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException("Can't find all tags", e);
        }
    }

    @Override
    public TagData findByName(String name) throws DaoException {
        try {
            Query query = entityManager.createQuery(FIND_BY_NAME_JQL, TagData.class);
            query.setParameter("name", name);
            return (TagData) query.getSingleResult();
        } catch (Exception e) {
            throw new DaoException("Can't find tag by name", e);
        }
    }

    @Override
    public Set<TagData> findByCertificate(CertificateData certificate) throws DaoException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<TagData> selectQuery = builder.createQuery(TagData.class);
            Root<TagData> root = selectQuery.from(TagData.class);
            selectQuery.where(builder.isMember(certificate, root.get("certificates")));

            TypedQuery<TagData> query = entityManager.createQuery(selectQuery);
            return query.getResultStream().collect(Collectors.toSet());

        } catch (Exception e) {
            throw new DaoException("Can't find tag by certificate id", e);
        }
    }

    @Override
    public List<TagData> findByNames(List<String> names) throws DaoException {
        try {
            if (names.isEmpty()) {
                return new ArrayList<>();
            }
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<TagData> selectQuery = builder.createQuery(TagData.class);
            Root<TagData> root = selectQuery.from(TagData.class);
            selectQuery.where(root.get("name").in(names));

            TypedQuery<TagData> query = entityManager.createQuery(selectQuery);
            return query.getResultList();

        } catch (Exception e) {
            throw new DaoException("Can't find tags by names", e);
        }
    }

    @Override
    public Set<TagData> saveAll(Set<TagData> tags) throws DaoException {
        try {
            if (tags.isEmpty()) {
                return new HashSet<>();
            }
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<TagData> findExistsQuery = builder.createQuery(TagData.class);
            Root<TagData> root = findExistsQuery.from(TagData.class);
            Predicate condition = createCondition(tags, builder, root);
            findExistsQuery.where(condition);

            List<TagData> existTags = entityManager.createQuery(findExistsQuery).getResultList();
            List<String> existNames = existTags.stream()
                    .map(TagData::getName)
                    .collect(Collectors.toList());

            Set<TagData> savedTags = tags.stream()
                    .filter(tag -> !existNames.contains(tag.getName()))
                    .peek(entityManager::persist)
                    .collect(Collectors.toSet());

            savedTags.addAll(existTags);
            return savedTags;
        } catch (Exception e) {
            throw new DaoException("Can't save all tags to db", e);
        }
    }

    private Predicate createCondition(Set<TagData> tags,
                                      CriteriaBuilder builder,
                                      Root<TagData> root) throws DaoException {
        return tags.stream()
                .map(TagData::getName)
                .map(name -> builder.equal(root.get("name"), name))
                .reduce(builder::or)
                .orElseThrow(DaoException::new);
    }
}
