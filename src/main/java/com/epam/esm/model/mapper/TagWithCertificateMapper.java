package com.epam.esm.model.mapper;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import static com.epam.esm.model.EntityUtils.UNDEFINED_ID;

public class TagWithCertificateMapper implements RowMapper<Tag> {
    private final RowMapper<Tag> tagMapper;
    private final RowMapper<Certificate> certificateMapper;

    public TagWithCertificateMapper(RowMapper<Tag> tagMapper, RowMapper<Certificate> certificateMapper) {
        this.tagMapper = tagMapper;
        this.certificateMapper = certificateMapper;
    }

    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tag tag = tagMapper.mapRow(rs, rowNum);
        Certificate certificate = certificateMapper.mapRow(rs, rowNum);
        if (certificate.getId() != UNDEFINED_ID) {
            Set<Certificate> certificates = tag.getCertificates();
            certificates.add(certificate);
        }
        return tag;
    }
}
