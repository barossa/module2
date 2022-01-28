package com.epam.esm.model.dao.impl;

import com.epam.esm.model.PropertyCombiner;
import com.epam.esm.model.dao.CertificateDao;
import com.epam.esm.model.dao.QueryUtils;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.dto.CertificateData;
import com.epam.esm.model.dto.TagData;
import com.epam.esm.model.exception.DaoException;
import com.epam.esm.model.mapper.CertificateMapper;
import com.epam.esm.model.mapper.CertificateTagsPropertyCombiner;
import com.epam.esm.model.mapper.CertificateWithTagMapper;
import com.epam.esm.model.mapper.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.epam.esm.model.dao.ColumnName.*;
import static com.epam.esm.model.dao.TableName.*;

@Component
public class CertificateDaoImpl implements CertificateDao {
    private static final String INSERT_CERTIFICATES_TAG = "INSERT INTO " + CERTIFICATE_TAGS + " (" + CERTIFICATE_TAGS_CERTIFICATE_ID + "," + CERTIFICATE_TAGS_TAG_ID + ") VALUES(?,?);";

    private static final String SELECT_CERTIFICATE_BY_ID = "SELECT " + CERTIFICATES_ID + "," + CERTIFICATES_NAME + "," + CERTIFICATES_DESCRIPTION + "," + CERTIFICATES_PRICE + ","
            + CERTIFICATES_DURATION + "," + CERTIFICATES_CREATE_DATE + "," + CERTIFICATES_LAST_UPDATE_DATE + " FROM " + CERTIFICATES
            + " WHERE " + CERTIFICATES_ID + "=?;";

    private static final String SELECT_ALL_CERTIFICATES = "SELECT " + CERTIFICATES_ID + "," + CERTIFICATES_NAME + "," + CERTIFICATES_DESCRIPTION + "," + CERTIFICATES_PRICE + ","
            + CERTIFICATES_DURATION + "," + CERTIFICATES_CREATE_DATE + "," + CERTIFICATES_LAST_UPDATE_DATE + "," + TAGS_ID + "," + TAGS_NAME + " FROM " + CERTIFICATES
            + " LEFT JOIN " + CERTIFICATE_TAGS + " ON " + CERTIFICATE_TAGS_CERTIFICATE_ID + "=" + CERTIFICATES_ID
            + " LEFT JOIN " + TAGS + " ON " + CERTIFICATE_TAGS_TAG_ID + "=" + TAGS_ID + ";";

    private static final String SELECT_CERTIFICATES_BY_TAG_ID = "SELECT " + CERTIFICATES_ID + "," + CERTIFICATES_NAME + "," + CERTIFICATES_DESCRIPTION + "," + CERTIFICATES_PRICE + ","
            + CERTIFICATES_DURATION + "," + CERTIFICATES_CREATE_DATE + "," + CERTIFICATES_LAST_UPDATE_DATE + "," + TAGS_ID + "," + TAGS_NAME + " FROM " + CERTIFICATES
            + " INNER JOIN " + CERTIFICATE_TAGS + " ON " + CERTIFICATE_TAGS_CERTIFICATE_ID + "=" + CERTIFICATES_ID
            + " INNER JOIN " + TAGS + " ON " + CERTIFICATE_TAGS_TAG_ID + "=" + TAGS_ID
            + " WHERE " + CERTIFICATE_TAGS_TAG_ID + "=?;";

    private static final String SELECT_CERTIFICATES_NOT_COMPLETED = "SELECT " + CERTIFICATES_ID + "," + CERTIFICATES_NAME + "," + CERTIFICATES_DESCRIPTION + "," + CERTIFICATES_PRICE + ","
            + CERTIFICATES_DURATION + "," + CERTIFICATES_CREATE_DATE + "," + CERTIFICATES_LAST_UPDATE_DATE + "," + TAGS_ID + "," + TAGS_NAME + " FROM " + CERTIFICATES
            + " LEFT JOIN " + CERTIFICATE_TAGS + " ON " + CERTIFICATE_TAGS_CERTIFICATE_ID + "=" + CERTIFICATES_ID
            + " LEFT JOIN " + TAGS + " ON " + CERTIFICATE_TAGS_TAG_ID + "=" + TAGS_ID
            + " WHERE";

    private static final String UPDATE_CERTIFICATE = "UPDATE " + CERTIFICATES + " SET " + CERTIFICATES_NAME + "=?," + CERTIFICATES_DESCRIPTION + "=?," + CERTIFICATES_PRICE + "=?,"
            + CERTIFICATES_DURATION + "=?," + CERTIFICATES_CREATE_DATE + "=?," + CERTIFICATES_LAST_UPDATE_DATE + "=?"
            + " WHERE " + CERTIFICATES_ID + "=?;";

    private static final String DELETE_CERTIFICATE_BY_ID = "DELETE FROM " + CERTIFICATES + " WHERE " + CERTIFICATES_ID + "=?;";

    private static final String DELETE_CERTIFICATES_TAG = "DELETE FROM " + CERTIFICATE_TAGS + " WHERE " + CERTIFICATE_TAGS_TAG_ID + "=? AND " + CERTIFICATE_TAGS_CERTIFICATE_ID + "=?;";

    private final JdbcTemplate jdbcTemplate;
    private final TagDao tagDao;
    private final PropertyCombiner<CertificateData> propertyCombiner;

    @Autowired
    public CertificateDaoImpl(JdbcTemplate jdbcTemplate, TagDao tagDao, PropertyCombiner<CertificateData> propertyCombiner) {
        this.jdbcTemplate = jdbcTemplate;
        this.tagDao = tagDao;
        this.propertyCombiner = propertyCombiner;
    }


    @Override
    public CertificateData find(int id) throws DaoException {
        try {
            List<CertificateData> certificates = jdbcTemplate.query(SELECT_CERTIFICATE_BY_ID, new CertificateMapper(), id);
            return certificates.stream()
                    .findFirst()
                    .orElse(null);
        } catch (DataAccessException e) {
            throw new DaoException("Can't find certificate by id", e);
        }
    }

    @Override
    @Transactional
    public CertificateData save(CertificateData certificate) throws DaoException {
        try {
            LocalDateTime now = LocalDateTime.now();
            certificate.setLastUpdateDate(now);
            certificate.setCreateDate(now);

            SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
            insert.withTableName(CERTIFICATES)
                    .usingGeneratedKeyColumns(CERTIFICATES_ID);

            Map<String, Object> parameters = getCertificateParameterMap(certificate);
            Number generatedKey = insert.executeAndReturnKey(parameters);
            certificate.setId(generatedKey.intValue());
            attachTagsToCertificate(certificate.getTags(), certificate);
            return certificate;

        } catch (DataAccessException e) {
            throw new DaoException("Can't save certificate", e);
        }
    }

    @Override
    public int delete(int id) throws DaoException {
        try {
            return jdbcTemplate.update(DELETE_CERTIFICATE_BY_ID, id);
        } catch (DataAccessException e) {
            throw new DaoException("Can't delete certificate", e);
        }
    }

    @Override
    public List<CertificateData> findAll() throws DaoException {
        try {
            RowMapper<CertificateData> certificateMapper = new CertificateMapper();
            RowMapper<TagData> tagMapper = new TagMapper();
            List<CertificateData> certificateRows = jdbcTemplate.query(SELECT_ALL_CERTIFICATES, new CertificateWithTagMapper(certificateMapper, tagMapper));
            return propertyCombiner.combine(certificateRows);
        } catch (DataAccessException e) {
            throw new DaoException("Can't find all certificates", e);
        }
    }

    @Override
    @Transactional
    public int update(CertificateData certificate) throws DaoException {
        try {
            Set<TagData> oldCertificateTags = tagDao.findByCertificateId(certificate.getId());
            Set<TagData> newCertificateTags = certificate.getTags();

            Set<TagData> tagsToAttach = newCertificateTags.stream()
                    .filter(tag -> !oldCertificateTags.contains(tag))
                    .collect(Collectors.toSet());
            Set<TagData> tagsToDetach = oldCertificateTags.stream()
                    .filter(tag -> !newCertificateTags.contains(tag))
                    .collect(Collectors.toSet());

            attachTagsToCertificate(tagsToAttach, certificate);
            detachTagsFromCertificate(tagsToDetach, certificate);

            certificate.setLastUpdateDate(LocalDateTime.now());
            return jdbcTemplate.update(UPDATE_CERTIFICATE, certificate.getName(), certificate.getDescription(), certificate.getPrice(),
                    certificate.getDuration(), certificate.getCreateDate(), certificate.getLastUpdateDate(), certificate.getId());
        } catch (DataAccessException e) {
            throw new DaoException("Can't update certificate", e);
        }
    }

    @Override
    public List<CertificateData> findByOptions(List<String> tags, List<String> names, List<String> descriptions, boolean strong) throws DaoException {
        try {
            String query = QueryUtils.buildSelectByOptions(SELECT_CERTIFICATES_NOT_COMPLETED, tags, names, descriptions, strong);
            RowMapper<TagData> tagDataMapper = new TagMapper();
            RowMapper<CertificateData> certificateDataMapper = new CertificateMapper();
            RowMapper<CertificateData> certificateWithTagDataMapper = new CertificateWithTagMapper(certificateDataMapper, tagDataMapper);
            List<CertificateData> certificateDataRows = jdbcTemplate.query(query, certificateWithTagDataMapper);
            CertificateTagsPropertyCombiner propertyCombiner = new CertificateTagsPropertyCombiner();
            return propertyCombiner.combine(certificateDataRows);

        } catch (DataAccessException e) {
            throw new DaoException("Can't find certificates data by options");
        }
    }

    @Override
    public List<CertificateData> findByTagId(int id) throws DaoException {
        try {
            RowMapper<CertificateData> certificateMapper = new CertificateMapper();
            RowMapper<TagData> tagMapper = new TagMapper();
            List<CertificateData> certificateRows = jdbcTemplate.query(SELECT_CERTIFICATES_BY_TAG_ID, new CertificateWithTagMapper(certificateMapper, tagMapper), id);
            return propertyCombiner.combine(certificateRows);
        } catch (DataAccessException e) {
            throw new DaoException("Can't find certificates by tag id", e);
        }
    }

    private Map<String, Object> getCertificateParameterMap(CertificateData certificate) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(getColumnName(CERTIFICATES_NAME), certificate.getName());
        parameters.put(getColumnName(CERTIFICATES_DESCRIPTION), certificate.getDescription());
        parameters.put(getColumnName(CERTIFICATES_PRICE), certificate.getPrice());
        parameters.put(getColumnName(CERTIFICATES_DURATION), certificate.getDuration());
        parameters.put(getColumnName(CERTIFICATES_CREATE_DATE), certificate.getCreateDate());
        parameters.put(getColumnName(CERTIFICATES_LAST_UPDATE_DATE), certificate.getLastUpdateDate());
        return parameters;
    }

    private void attachTagsToCertificate(Set<TagData> tags, CertificateData certificate) throws DaoException {
        try {
            jdbcTemplate.batchUpdate(INSERT_CERTIFICATES_TAG, new InsertCertificateTagBatchSetterImpl(new ArrayList<>(tags), certificate));
        } catch (DataAccessException e) {
            throw new DaoException("Can't attach tags to certificate", e);
        }
    }

    private void detachTagsFromCertificate(Set<TagData> tags, CertificateData certificate) throws DaoException {
        try {
            jdbcTemplate.batchUpdate(DELETE_CERTIFICATES_TAG, new DeleteCertificateTagBatchSetterImpl(new ArrayList<>(tags), certificate));
        } catch (DataAccessException e) {
            throw new DaoException("Can't detach tags from certificate", e);
        }
    }
}
