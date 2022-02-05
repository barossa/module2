package com.epam.esm.model.util;

import com.epam.esm.model.dto.CertificateData;
import com.epam.esm.model.dto.TagData;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class InsertCertificateTagBatchSetterImpl implements BatchPreparedStatementSetter {
    private final List<TagData> tags;
    private final CertificateData certificate;

    public InsertCertificateTagBatchSetterImpl(List<TagData> tags, CertificateData certificate) {
        this.tags = tags;
        this.certificate = certificate;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        TagData tag = tags.get(i);
        ps.setInt(1, certificate.getId());
        ps.setInt(2, tag.getId());
    }

    @Override
    public int getBatchSize() {
        return tags.size();
    }
}
