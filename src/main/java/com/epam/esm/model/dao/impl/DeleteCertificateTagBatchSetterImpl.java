package com.epam.esm.model.dao.impl;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DeleteCertificateTagBatchSetterImpl implements BatchPreparedStatementSetter {
    private final List<Tag> tags;
    private final Certificate certificate;

    public DeleteCertificateTagBatchSetterImpl(List<Tag> tags, Certificate certificate) {
        this.tags = tags;
        this.certificate = certificate;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        Tag tag = tags.get(i);
        ps.setInt(1, tag.getId());
        ps.setInt(2, certificate.getId());
    }

    @Override
    public int getBatchSize() {
        return tags.size();
    }
}
