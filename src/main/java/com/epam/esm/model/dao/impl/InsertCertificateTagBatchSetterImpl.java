package com.epam.esm.model.dao.impl;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class InsertCertificateTagBatchSetterImpl implements BatchPreparedStatementSetter {
    private final List<Tag> tags;
    private final Certificate certificate;

    public InsertCertificateTagBatchSetterImpl(List<Tag> tags, Certificate certificate) {
        this.tags = tags;
        this.certificate = certificate;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        Tag tag = tags.get(i);
        ps.setInt(1, certificate.getId());
        ps.setInt(2, tag.getId());
    }

    @Override
    public int getBatchSize() {
        return tags.size();
    }
}
