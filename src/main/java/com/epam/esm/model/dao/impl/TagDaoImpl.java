package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.exception.DaoException;
import com.epam.esm.model.mapper.TagMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.epam.esm.model.dao.ColumnName.*;
import static com.epam.esm.model.dao.TableName.*;

@Component
public class TagDaoImpl implements TagDao {
    private static final String SELECT_TAG_BY_ID = "SELECT " + TAGS_ID + "," + TAGS_NAME + " FROM " + TAGS
            + " WHERE " + TAGS_ID + "=?;";

    private static final String SELECT_ALL_TAGS = "SELECT " + TAGS_ID + "," + TAGS_NAME + " FROM " + TAGS + ";";

    private static final String SELECT_TAG_BY_NAME = "SELECT " + TAGS_ID + "," + TAGS_NAME + " FROM " + TAGS
            + " WHERE " + TAGS_NAME + "=?;";

    private static final String SELECT_TAGS_BY_CERTIFICATE_ID = "SELECT " + TAGS_ID + "," + TAGS_NAME
            + " FROM " + CERTIFICATES
            + " INNER JOIN " + CERTIFICATE_TAGS + " ON " + CERTIFICATE_TAGS_TAG_ID + "=" + TAGS_ID
            + " WHERE " + CERTIFICATE_TAGS_CERTIFICATE_ID + "=?;";

    private static final String DELETE_TAG_BY_ID = "DELETE FROM " + TAGS + " WHERE " + TAGS_ID + "=?;";

    private final JdbcTemplate jdbcTemplate;

    public TagDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Tag find(int id) throws DaoException {
        try {
            return jdbcTemplate.query(SELECT_TAG_BY_ID, new TagMapper(), id).stream()
                    .findFirst()
                    .orElse(null);
        } catch (DataAccessException e) {
            throw new DaoException("Can't find tag", e);
        }
    }

    @Override
    public Tag save(Tag tag) throws DaoException {
        try {
            SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
            insert.withTableName(TAGS)
                    .usingGeneratedKeyColumns(TAGS_ID);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put(TAGS_NAME, tag.getName());

            Number generatedKey = insert.executeAndReturnKey(parameters);
            tag.setId(generatedKey.intValue());
            return tag;
        } catch (DataAccessException e) {
            throw new DaoException("Can't save tag", e);
        }
    }

    @Override
    public int delete(Tag tag) throws DaoException {
        try {
            return jdbcTemplate.update(DELETE_TAG_BY_ID, tag.getId());
        } catch (DataAccessException e) {
            throw new DaoException("Can't delete tag", e);
        }
    }

    @Override
    public List<Tag> findAll() throws DaoException {
        try {
            return jdbcTemplate.query(SELECT_ALL_TAGS, new TagMapper());
        } catch (DataAccessException e) {
            throw new DaoException("Can't find all tags", e);
        }
    }

    @Override
    public Tag findByName(String name) throws DaoException {
        try {
            return jdbcTemplate.query(SELECT_TAG_BY_NAME, new TagMapper(), name).stream()
                    .findFirst()
                    .orElse(null);
        } catch (DataAccessException e) {
            throw new DaoException("Can't find tag by name", e);
        }
    }

    @Override
    public List<Tag> findByCertificateId(int id) throws DaoException {
        try{
            return jdbcTemplate.query(SELECT_TAGS_BY_CERTIFICATE_ID, new TagMapper(), id);
        }catch (DataAccessException e){
            throw new DaoException("Can't find tag by certificate id", e);
        }
    }
}
