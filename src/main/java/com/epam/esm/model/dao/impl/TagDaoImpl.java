package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.exception.DaoException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.epam.esm.model.dao.ColumnName.TAGS_ID;
import static com.epam.esm.model.dao.ColumnName.TAGS_NAME;
import static com.epam.esm.model.dao.TableName.TAGS;

@Component
public class TagDaoImpl implements TagDao {
    private static final String FIND_TAG_BY_ID = "SELECT " + TAGS_ID + "," + TAGS_NAME + " FROM " + TAGS
            + " WHERE " + TAGS_ID + "=?;";

    private static final String DELETE_TAG_BY_ID = "DELETE FROM " + TAGS + " WHERE " + TAGS_ID + "=?;";

    private static final String FIND_TAG_BY_NAME = "SELECT " + TAGS_ID + "," + TAGS_NAME + " FROM " + TAGS
            + " WHERE " + TAGS_ID + "=?;";

    private final JdbcTemplate jdbcTemplate;

    public TagDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Tag find(int id) throws DaoException {
        try {
            return jdbcTemplate.query(FIND_TAG_BY_ID, new BeanPropertyRowMapper<>(Tag.class), id).stream()
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
    public Tag findByName(String name) throws DaoException {
        try{
            return jdbcTemplate.query(FIND_TAG_BY_NAME, new BeanPropertyRowMapper<>(Tag.class), name).stream()
                    .findFirst()
                    .orElse(null);
        }catch (DataAccessException e){
            throw new DaoException("Can't find tag by name", e);
        }
    }
}
