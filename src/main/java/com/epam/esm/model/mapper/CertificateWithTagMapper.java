package com.epam.esm.model.mapper;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import static com.epam.esm.model.EntityUtils.UNDEFINED_ID;

public class CertificateWithTagMapper implements RowMapper<Certificate> {
    private final RowMapper<Certificate> certificateMapper;
    private final RowMapper<Tag> tagMapper;

    public CertificateWithTagMapper(RowMapper<Certificate> certificateMapper, RowMapper<Tag> tagMapper) {
        this.certificateMapper = certificateMapper;
        this.tagMapper = tagMapper;
    }

    @Override
    public Certificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        Certificate certificate = certificateMapper.mapRow(rs, rowNum);
        Tag tag = tagMapper.mapRow(rs, rowNum);
        if (tag.getId() != UNDEFINED_ID) {
            Set<Tag> tags = certificate.getTags();
            tags.add(tag);
        }
        return certificate;
    }
}
