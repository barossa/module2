package com.epam.esm.model.mapper;

import com.epam.esm.model.dto.TagData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.epam.esm.model.dao.ColumnName.TAGS_ID;
import static com.epam.esm.model.dao.ColumnName.TAGS_NAME;

public class TagMapper implements RowMapper<TagData> {
    @Override
    public TagData mapRow(ResultSet rs, int rowNum) throws SQLException {
        TagData tag = new TagData();
        tag.setId(rs.getInt(TAGS_ID));
        tag.setName(rs.getString(TAGS_NAME));
        return tag;
    }
}
