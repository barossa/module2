package com.epam.esm.model.mapper;

import com.epam.esm.model.dto.CertificateData;
import com.epam.esm.model.dto.TagData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import static com.epam.esm.model.EntityUtils.UNDEFINED_ID;

public class TagWithCertificateMapper implements RowMapper<TagData> {
    private final RowMapper<TagData> tagMapper;
    private final RowMapper<CertificateData> certificateMapper;

    public TagWithCertificateMapper(RowMapper<TagData> tagMapper, RowMapper<CertificateData> certificateMapper) {
        this.tagMapper = tagMapper;
        this.certificateMapper = certificateMapper;
    }

    @Override
    public TagData mapRow(ResultSet rs, int rowNum) throws SQLException {
        TagData tag = tagMapper.mapRow(rs, rowNum);
        CertificateData certificate = certificateMapper.mapRow(rs, rowNum);
        if (certificate.getId() != UNDEFINED_ID) {
            Set<CertificateData> certificates = tag.getCertificates();
            certificates.add(certificate);
        }
        return tag;
    }
}
