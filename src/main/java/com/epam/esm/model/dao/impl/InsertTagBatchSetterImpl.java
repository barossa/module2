package com.epam.esm.model.dao.impl;

import com.epam.esm.model.entity.Tag;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class InsertTagBatchSetterImpl implements BatchPreparedStatementSetter {
    private final List<Tag> tags;

    public InsertTagBatchSetterImpl(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        Tag tag = tags.get(i);
        ps.setString(1, tag.getName());
    }

    @Override
    public int getBatchSize() {
        return tags.size();
    }
}
