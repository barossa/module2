package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.CertificateDao;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.exception.DaoException;
import com.epam.esm.model.mapper.CertificateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.epam.esm.model.dao.ColumnName.*;
import static com.epam.esm.model.dao.TableName.CERTIFICATES;
import static com.epam.esm.model.dao.TableName.CERTIFICATE_TAGS;

@Component
public class CertificateDaoImpl implements CertificateDao {
    private static final String SELECT_CERTIFICATE_BY_ID = "SELECT " + CERTIFICATES_ID + "," + CERTIFICATES_NAME + "," + CERTIFICATES_DESCRIPTION + "," + CERTIFICATES_PRICE + ","
            + CERTIFICATES_DURATION + "," + CERTIFICATES_CREATE_DATE + "," + CERTIFICATES_LAST_UPDATE_DATE + " FROM " + CERTIFICATES
            + " WHERE " + CERTIFICATES_ID + "=?;";

    private static final String SELECT_ALL_CERTIFICATES = "SELECT " + CERTIFICATES_ID + "," + CERTIFICATES_NAME + "," + CERTIFICATES_DESCRIPTION + "," + CERTIFICATES_PRICE + ","
            + CERTIFICATES_DURATION + "," + CERTIFICATES_CREATE_DATE + "," + CERTIFICATES_LAST_UPDATE_DATE + " FROM " + CERTIFICATES + ";";

    private static final String SELECT_CERTIFICATES_BY_TAG_ID = "SELECT " + CERTIFICATES_ID + "," + CERTIFICATES_NAME + "," + CERTIFICATES_DESCRIPTION + "," + CERTIFICATES_PRICE + ","
            + CERTIFICATES_DURATION + "," + CERTIFICATES_CREATE_DATE + "," + CERTIFICATES_LAST_UPDATE_DATE + " FROM " + CERTIFICATES
            + " INNER JOIN " + CERTIFICATE_TAGS + " ON " + CERTIFICATE_TAGS_CERTIFICATE_ID + "=" + CERTIFICATES_ID
            + " WHERE " + CERTIFICATE_TAGS_TAG_ID + "=?;";


    private static final String DELETE_CERTIFICATE_BY_ID = "DELETE FROM " + CERTIFICATES + " WHERE " + CERTIFICATES_ID + "=?;";

    private static final String UPDATE_CERTIFICATE = "UPDATE " + CERTIFICATES + " SET " + CERTIFICATES_NAME + "=?," + CERTIFICATES_DESCRIPTION + "=?," + CERTIFICATES_PRICE + "=?,"
            + CERTIFICATES_DURATION + "=?," + CERTIFICATES_CREATE_DATE + "=?," + CERTIFICATES_LAST_UPDATE_DATE + "=?"
            + " WHERE " + CERTIFICATES_ID + "=?;";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CertificateDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Certificate find(int id) throws DaoException {
        try {
            List<Certificate> certificates = jdbcTemplate.query(SELECT_CERTIFICATE_BY_ID, new CertificateMapper(), id);
            return certificates.stream()
                    .findFirst()
                    .orElse(null);
        } catch (DataAccessException e) {
            throw new DaoException("Can't find certificate by id", e);
        }
    }

    @Override
    public Certificate save(Certificate certificate) throws DaoException {
        try {
            SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
            insert.withTableName(CERTIFICATES)
                    .usingGeneratedKeyColumns(CERTIFICATES_ID);

            Map<String, Object> parameters = getCertificateParameterMap(certificate);
            Number generatedKey = insert.executeAndReturnKey(parameters);
            certificate.setId(generatedKey.intValue());
            return certificate;

        } catch (DataAccessException e) {
            throw new DaoException("Can't save certificate", e);
        }
    }

    @Override
    public int delete(Certificate certificate) throws DaoException {
        try {
            return jdbcTemplate.update(DELETE_CERTIFICATE_BY_ID, certificate.getId());
        } catch (DataAccessException e) {
            throw new DaoException("Can't delete certificate", e);
        }
    }

    @Override
    public List<Certificate> findAll() throws DaoException {
        try {
            return jdbcTemplate.query(SELECT_ALL_CERTIFICATES, new CertificateMapper());
        } catch (DataAccessException e) {
            throw new DaoException("Can't find all certificates", e);
        }
    }

    @Override
    public int update(Certificate certificate) throws DaoException {
        try {
            return jdbcTemplate.update(UPDATE_CERTIFICATE, certificate.getName(), certificate.getDescription(), certificate.getPrice(),
                    certificate.getDuration(), certificate.getCreateDate(), certificate.getLastUpdateDate(), certificate.getId());
        } catch (DataAccessException e) {
            throw new DaoException("Can't update certificate", e);
        }
    }

    @Override
    public List<Certificate> findByTagId(int id) throws DaoException {
        try {
            return jdbcTemplate.query(SELECT_CERTIFICATES_BY_TAG_ID, new CertificateMapper(), id);
        } catch (DataAccessException e) {
            throw new DaoException("Can't find certificates by tag id", e);
        }
    }

    private Map<String, Object> getCertificateParameterMap(Certificate certificate) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(CERTIFICATES_NAME, certificate.getName());
        parameters.put(CERTIFICATES_DESCRIPTION, certificate.getDescription());
        parameters.put(CERTIFICATES_PRICE, certificate.getPrice());
        parameters.put(CERTIFICATES_DURATION, certificate.getDuration());
        parameters.put(CERTIFICATES_CREATE_DATE, certificate.getCreateDate());
        parameters.put(CERTIFICATES_LAST_UPDATE_DATE, certificate.getLastUpdateDate());
        return parameters;
    }
}
