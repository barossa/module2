package com.epam.esm.model.mapper;

import com.epam.esm.model.dto.CertificateData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.epam.esm.model.EntityUtils.UNDEFINED_ID;
import static com.epam.esm.model.dao.ColumnName.*;

public class CertificateMapper implements RowMapper<CertificateData> {
    @Override
    public CertificateData mapRow(ResultSet rs, int rowNum) throws SQLException {
        CertificateData certificate = new CertificateData();
        certificate.setId(rs.getInt(CERTIFICATES_ID));
        if(certificate.getId() != UNDEFINED_ID){
            certificate.setName(rs.getString(CERTIFICATES_NAME));
            certificate.setDescription(rs.getString(CERTIFICATES_DESCRIPTION));
            certificate.setPrice(rs.getBigDecimal(CERTIFICATES_PRICE));
            certificate.setDuration(rs.getLong(CERTIFICATES_DURATION));
            certificate.setCreateDate(rs.getTimestamp(CERTIFICATES_CREATE_DATE).toLocalDateTime());
            certificate.setLastUpdateDate(rs.getTimestamp(CERTIFICATES_LAST_UPDATE_DATE).toLocalDateTime());
        }
        return certificate;
    }
}
