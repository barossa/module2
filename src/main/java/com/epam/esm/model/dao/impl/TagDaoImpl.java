package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.exception.DaoException;
import com.epam.esm.model.mapper.TagMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.epam.esm.model.dao.ColumnName.*;
import static com.epam.esm.model.dao.TableName.*;

@Component
public class TagDaoImpl implements TagDao {
    private static final String INSERT_TAG = "INSERT IGNORE INTO " + TAGS + " (" + TAGS_NAME + ") VALUES(?);";

    private static final String SELECT_TAG_BY_ID = "SELECT " + TAGS_ID + "," + TAGS_NAME + " FROM " + TAGS
            + " WHERE " + TAGS_ID + "=?;";

    private static final String SELECT_ALL_TAGS = "SELECT " + TAGS_ID + "," + TAGS_NAME + " FROM " + TAGS + ";";

    private static final String SELECT_TAG_BY_NAME_NOT_COMPLETED = "SELECT " + TAGS_ID + "," + TAGS_NAME + " FROM " + TAGS
            + " WHERE";

    private static final String SELECT_TAGS_BY_CERTIFICATE_ID = "SELECT " + TAGS_ID + "," + TAGS_NAME
            + " FROM " + CERTIFICATES
            + " INNER JOIN " + CERTIFICATE_TAGS + " ON " + CERTIFICATE_TAGS_CERTIFICATE_ID + "=" + CERTIFICATES_ID
            + " INNER JOIN " + TAGS + " ON " + TAGS_ID + "=" + CERTIFICATE_TAGS_TAG_ID
            + " WHERE " + CERTIFICATE_TAGS_CERTIFICATE_ID + "=?;";

    private static final String DELETE_TAG_BY_ID = "DELETE FROM " + TAGS + " WHERE " + TAGS_ID + "=?;";

    private static final String SQL_OR = "OR";
    private static final String SQL_EQUALS = "=";
    private static final String SQL_QUOTE = "'";

    private final JdbcTemplate jdbcTemplate;

    public TagDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Tag find(int id) throws DaoException {
        try {
            return jdbcTemplate.query(SELECT_TAG_BY_ID, new TagMapper(), id).stream()
                    .findAny()
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
            return findTagsByName(Collections.singletonList(name)).stream()
                    .findFirst()
                    .orElse(null);
        } catch (DataAccessException e) {
            throw new DaoException("Can't find tag by name", e);
        }
    }

    @Override
    public List<Tag> findByCertificateId(int id) throws DaoException {
        try {
            return jdbcTemplate.query(SELECT_TAGS_BY_CERTIFICATE_ID, new TagMapper(), id);
        } catch (DataAccessException e) {
            throw new DaoException("Can't find tag by certificate id", e);
        }
    }

    @Override
    @Transactional
    public Set<Tag> saveAll(Set<Tag> tags) throws DaoException {
        try {
            jdbcTemplate.batchUpdate(INSERT_TAG, new InsertTagBatchSetterImpl(new ArrayList<>(tags)));
            List<String> tagNames = tags.stream()
                    .map(Tag::getName)
                    .collect(Collectors.toList());
            List<Tag> savedTags = findTagsByName(tagNames);
            return new HashSet<>(savedTags);

        } catch (DataAccessException e) {
            throw new DaoException("Can't save all tags", e);
        }
    }

    private List<Tag> findTagsByName(List<String> names) throws DaoException {
        try {
            return jdbcTemplate.query(buildSelectByNamesQuery(names), new TagMapper());
        } catch (DataAccessException e) {
            throw new DaoException("Can't find tags by name", e);
        }
    }

    private String buildSelectByNamesQuery(List<String> names) {
        StringBuilder builder = new StringBuilder(SELECT_TAG_BY_NAME_NOT_COMPLETED);
        ListIterator<String> iterator = names.listIterator();
        while (iterator.hasNext()) {
            builder.append(" ")
                    .append(TAGS_NAME)
                    .append(SQL_EQUALS)
                    .append(SQL_QUOTE)
                    .append(iterator.next())
                    .append(SQL_QUOTE);
            builder.append(iterator.hasNext() ? " " + SQL_OR : ";");
        }
        return builder.toString();
    }
}
