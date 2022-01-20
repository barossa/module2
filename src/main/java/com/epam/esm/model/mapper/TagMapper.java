package com.epam.esm.model.mapper;

import com.epam.esm.model.entity.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.epam.esm.model.dao.ColumnName.TAGS_ID;
import static com.epam.esm.model.dao.ColumnName.TAGS_NAME;

public class TagMapper implements RowMapper<Tag> {
    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tag tag = new Tag();
        tag.setId(rs.getInt(TAGS_ID));
        tag.setName(rs.getString(TAGS_NAME));
        return tag;
    }
}
