package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.CertificateDao;
import com.epam.esm.model.dto.CertificateData;
import com.epam.esm.model.dto.CertificateFilter;
import com.epam.esm.model.dto.CertificateSort;
import com.epam.esm.model.dto.PageData;
import com.epam.esm.model.exception.DaoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class CertificateDaoImpl implements CertificateDao {
    private static final String FIND_ALL_CERTIFICATES_JQL = "SELECT c FROM CertificateData c";
    private static final String DELETE_CERTIFICATE_JQL = "DELETE FROM CertificateData c WHERE c.id = :id";

    private static final String PERCENT = "%";

    private final EntityManager entityManager;

    @Override
    public CertificateData find(int id) throws DaoException {
        try {
            return entityManager.find(CertificateData.class, id);
        } catch (Exception e) {
            throw new DaoException("can't find certificate by id", e);
        }
    }

    @Override
    @Transactional
    public CertificateData save(CertificateData certificateData) throws DaoException {
        try {
            entityManager.persist(certificateData);
            return certificateData;
        } catch (Exception e) {
            throw new DaoException("Can't save certificate to db", e);
        }
    }

    @Override
    @Transactional
    public int delete(int id) throws DaoException {
        try {
            Query query = entityManager.createQuery(DELETE_CERTIFICATE_JQL);
            query.setParameter("id", id);
            return query.executeUpdate();
        } catch (Exception e) {
            throw new DaoException("Can't delete certificate from db", e);
        }
    }

    @Override
    public List<CertificateData> findAll(PageData page) throws DaoException {
        try {
            TypedQuery<CertificateData> query = entityManager.createQuery(FIND_ALL_CERTIFICATES_JQL, CertificateData.class);
            query.setFirstResult(page.getOffset());
            query.setMaxResults(page.getLimit());
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException("Can't find all certificates", e);
        }
    }

    @Override
    public CertificateData update(CertificateData certificate) throws DaoException {
        try {
            return entityManager.merge(certificate);
        } catch (Exception e) {
            throw new DaoException("Can't update certificate data", e);
        }
    }

    @Override
    @Transactional
    public List<CertificateData> findByFilter(CertificateFilter filter, PageData page, Set<CertificateSort> sorts) throws DaoException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<CertificateData> filterQuery = builder.createQuery(CertificateData.class);
            Root<CertificateData> root = filterQuery.from(CertificateData.class);

            Optional<Predicate> predicate = buildOptionsPredicate(builder, root, filter);
            predicate.ifPresent(filterQuery::where);
            List<Order> orders = buildSorts(sorts, builder, root);
            filterQuery.orderBy(orders);

            TypedQuery<CertificateData> query = entityManager.createQuery(filterQuery);
            query.setFirstResult(page.getOffset());
            query.setMaxResults(page.getLimit());
            return query.getResultList();

        } catch (Exception e) {
            throw new DaoException("Can't find certificates by options", e);
        }
    }

    private Optional<Predicate> buildOptionsPredicate(CriteriaBuilder builder,
                                                      Root<CertificateData> root,
                                                      CertificateFilter filter
    ) {
        Optional<Predicate> tagsPredicate = filter.getTags().stream()
                .map(tag -> builder.isMember(tag, root.get("tags")))
                .reduce(builder::and);
        Optional<Predicate> namesPredicate = filter.getNames().stream()
                .map(name -> builder.like(root.get("name"), PERCENT + name + PERCENT))
                .reduce(builder::and);
        Optional<Predicate> descriptionsPredicate = filter.getDescriptions().stream()
                .map(description -> builder.like(root.get("description"), PERCENT + description + PERCENT))
                .reduce(builder::and);
        return Stream.of(tagsPredicate, namesPredicate, descriptionsPredicate)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .reduce(builder::and);
    }


    private List<Order> buildSorts(Set<CertificateSort> sorts,
                                   CriteriaBuilder builder,
                                   Root<CertificateData> root) {
        Function<CertificateSort, Order> converter = s -> {
            Path<Object> field = root.get(s.getFieldName());
            return s.isAscending() ? builder.asc(field) : builder.desc(field);
        };

        return sorts.stream()
                .map(converter)
                .collect(Collectors.toList());
    }
}