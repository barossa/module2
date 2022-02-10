package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.CertificateDao;
import com.epam.esm.model.dto.CertificateData;
import com.epam.esm.model.dto.CertificateFilter;
import com.epam.esm.model.dto.PageData;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
            LocalDateTime now = LocalDateTime.now();
            certificateData.setCreateDate(now);
            certificateData.setLastUpdateDate(now);
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
            certificate.setLastUpdateDate(LocalDateTime.now());
            return entityManager.merge(certificate);
        } catch (Exception e) {
            throw new DaoException("Can't update certificate data", e);
        }
    }

    @Override
    @Transactional
    public List<CertificateData> findByFilter(CertificateFilter filter, PageData page) throws DaoException {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<CertificateData> optionsQuery = criteriaBuilder.createQuery(CertificateData.class);
            Root<CertificateData> root = optionsQuery.from(CertificateData.class);
            Optional<Predicate> predicate = buildOptionsPredicate(criteriaBuilder, root, filter);
            predicate.ifPresent(optionsQuery::where);

            TypedQuery<CertificateData> query = entityManager.createQuery(optionsQuery);
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
}