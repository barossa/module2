package com.epam.esm.model.util;

import com.epam.esm.model.dto.TagData;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class InsertTagBatchSetterImpl implements BatchPreparedStatementSetter {
    private final List<TagData> tags;

    public InsertTagBatchSetterImpl(List<TagData> tags) {
        this.tags = tags;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        TagData tag = tags.get(i);
        ps.setString(1, tag.getName());
    }

    @Override
    public int getBatchSize() {
        return tags.size();
    }
}
