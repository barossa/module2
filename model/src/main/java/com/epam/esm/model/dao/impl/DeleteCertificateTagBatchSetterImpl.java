package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dto.CertificateData;
import com.epam.esm.model.dto.TagData;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DeleteCertificateTagBatchSetterImpl implements BatchPreparedStatementSetter {
    private final List<TagData> tags;
    private final CertificateData certificate;

    public DeleteCertificateTagBatchSetterImpl(List<TagData> tags, CertificateData certificate) {
        this.tags = tags;
        this.certificate = certificate;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        TagData tag = tags.get(i);
        ps.setInt(1, tag.getId());
        ps.setInt(2, certificate.getId());
    }

    @Override
    public int getBatchSize() {
        return tags.size();
    }
}
