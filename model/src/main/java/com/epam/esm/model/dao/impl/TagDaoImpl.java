package com.epam.esm.model.dao.impl;

import com.epam.esm.model.util.PropertyCombiner;
import com.epam.esm.model.dao.QueryUtils;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.dto.CertificateData;
import com.epam.esm.model.dto.TagData;
import com.epam.esm.model.exception.DaoException;
import com.epam.esm.model.mapper.CertificateMapper;
import com.epam.esm.model.util.InsertTagBatchSetterImpl;
import com.epam.esm.model.mapper.TagMapper;
import com.epam.esm.model.mapper.TagWithCertificateMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.epam.esm.model.dao.ColumnName.*;
import static com.epam.esm.model.dao.TableName.*;

@Component
public class TagDaoImpl implements TagDao {
    private static final String INSERT_TAG = "INSERT IGNORE INTO " + TAGS + " (" + getColumnName(TAGS_NAME) + ") VALUES(?);";

    private static final String SELECT_TAG_BY_ID = "SELECT " + TAGS_ID + "," + TAGS_NAME + " FROM " + TAGS
            + " WHERE " + TAGS_ID + "=?;";

    private static final String SELECT_ALL_TAGS = "SELECT " + TAGS_ID + "," + TAGS_NAME + "," + CERTIFICATES_ID + "," + CERTIFICATES_NAME + "," + CERTIFICATES_DESCRIPTION + ","
            + CERTIFICATES_PRICE + "," + CERTIFICATES_DURATION + "," + CERTIFICATES_CREATE_DATE + "," + CERTIFICATES_LAST_UPDATE_DATE
            + " FROM " + TAGS
            + " LEFT JOIN " + CERTIFICATE_TAGS + " ON " + CERTIFICATE_TAGS_TAG_ID + "=" + TAGS_ID
            + " LEFT JOIN " + CERTIFICATES + " ON " + CERTIFICATES_ID + "=" + CERTIFICATE_TAGS_CERTIFICATE_ID + ";";

    private static final String SELECT_TAG_BY_NAME_NOT_COMPLETED = "SELECT " + TAGS_ID + "," + TAGS_NAME + " FROM " + TAGS
            + " WHERE";

    private static final String SELECT_TAGS_BY_CERTIFICATE_ID = "SELECT " + TAGS_ID + "," + TAGS_NAME
            + " FROM " + CERTIFICATES
            + " INNER JOIN " + CERTIFICATE_TAGS + " ON " + CERTIFICATE_TAGS_CERTIFICATE_ID + "=" + CERTIFICATES_ID
            + " INNER JOIN " + TAGS + " ON " + TAGS_ID + "=" + CERTIFICATE_TAGS_TAG_ID
            + " WHERE " + CERTIFICATE_TAGS_CERTIFICATE_ID + "=?;";

    private static final String DELETE_TAG_BY_ID = "DELETE FROM " + TAGS + " WHERE " + TAGS_ID + "=?;";

    private final JdbcTemplate jdbcTemplate;
    private final PropertyCombiner<TagData> tagPropertyCombiner;

    public TagDaoImpl(JdbcTemplate jdbcTemplate, PropertyCombiner<TagData> tagPropertyCombiner) {
        this.jdbcTemplate = jdbcTemplate;
        this.tagPropertyCombiner = tagPropertyCombiner;
    }

    @Override
    public TagData find(int id) throws DaoException {
        try {
            return jdbcTemplate.query(SELECT_TAG_BY_ID, new TagMapper(), id).stream()
                    .findAny()
                    .orElse(null);
        } catch (DataAccessException e) {
            throw new DaoException("Can't find tag", e);
        }
    }

    @Override
    public TagData save(TagData tag) throws DaoException {
        try {
            SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
            insert.withTableName(TAGS)
                    .usingGeneratedKeyColumns(getColumnName(TAGS_ID));

            Map<String, Object> parameters = new HashMap<>();
            parameters.put(getColumnName(TAGS_NAME), tag.getName());

            Number generatedKey = insert.executeAndReturnKey(parameters);
            tag.setId(generatedKey.intValue());
            return tag;
        } catch (DataAccessException e) {
            throw new DaoException("Can't save tag", e);
        }
    }

    @Override
    public int delete(int id) throws DaoException {
        try {
            return jdbcTemplate.update(DELETE_TAG_BY_ID, id);
        } catch (DataAccessException e) {
            throw new DaoException("Can't delete tag", e);
        }
    }

    @Override
    public List<TagData> findAll() throws DaoException {
        try {
            RowMapper<TagData> tagMapper = new TagMapper();
            RowMapper<CertificateData> certificateMapper = new CertificateMapper();
            List<TagData> tagRows = jdbcTemplate.query(SELECT_ALL_TAGS, new TagWithCertificateMapper(tagMapper, certificateMapper));
            return tagPropertyCombiner.combine(tagRows);
        } catch (DataAccessException e) {
            throw new DaoException("Can't find all tags", e);
        }
    }

    @Override
    public TagData findByName(String name) throws DaoException {
        try {
            return findTagsByName(Collections.singletonList(name)).stream()
                    .findFirst()
                    .orElse(null);
        } catch (DataAccessException e) {
            throw new DaoException("Can't find tag by name", e);
        }
    }

    @Override
    public Set<TagData> findByCertificateId(int id) throws DaoException {
        try {
            List<TagData> tags = jdbcTemplate.query(SELECT_TAGS_BY_CERTIFICATE_ID, new TagMapper(), id);
            return new HashSet<>(tags);
        } catch (DataAccessException e) {
            throw new DaoException("Can't find tag by certificate id", e);
        }
    }

    @Override
    @Transactional
    public Set<TagData> saveAll(Set<TagData> tags) throws DaoException {
        try {
            jdbcTemplate.batchUpdate(INSERT_TAG, new InsertTagBatchSetterImpl(new ArrayList<>(tags)));
            List<String> tagNames = tags.stream()
                    .map(TagData::getName)
                    .collect(Collectors.toList());
            List<TagData> savedTags = findTagsByName(tagNames);
            return new HashSet<>(savedTags);

        } catch (DataAccessException e) {
            throw new DaoException("Can't save all tags", e);
        }
    }

    private List<TagData> findTagsByName(List<String> names) throws DaoException {
        try {
            if (names.isEmpty()) {
                return new ArrayList<>();
            }
            String query = QueryUtils.buildSelectByTagNamesQuery(SELECT_TAG_BY_NAME_NOT_COMPLETED, names);
            return jdbcTemplate.query(query, new TagMapper());
        } catch (DataAccessException e) {
            throw new DaoException("Can't find tags by name", e);
        }
    }
}
