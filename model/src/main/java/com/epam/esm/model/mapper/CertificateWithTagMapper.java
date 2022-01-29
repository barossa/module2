package com.epam.esm.model.mapper;

import com.epam.esm.model.dto.CertificateData;
import com.epam.esm.model.dto.TagData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import static com.epam.esm.model.util.EntityUtils.UNDEFINED_ID;

public class CertificateWithTagMapper implements RowMapper<CertificateData> {
    private final RowMapper<CertificateData> certificateMapper;
    private final RowMapper<TagData> tagMapper;

    public CertificateWithTagMapper(RowMapper<CertificateData> certificateMapper, RowMapper<TagData> tagMapper) {
        this.certificateMapper = certificateMapper;
        this.tagMapper = tagMapper;
    }

    @Override
    public CertificateData mapRow(ResultSet rs, int rowNum) throws SQLException {
        CertificateData certificate = certificateMapper.mapRow(rs, rowNum);
        TagData tag = tagMapper.mapRow(rs, rowNum);
        if (tag.getId() != UNDEFINED_ID) {
            Set<TagData> tags = certificate.getTags();
            tags.add(tag);
        }
        return certificate;
    }
}
