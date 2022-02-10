package com.epam.esm.model.dao;

import com.epam.esm.model.dto.CertificateData;
import com.epam.esm.model.dto.CertificateFilter;
import com.epam.esm.model.dto.PageData;
import com.epam.esm.model.exception.DaoException;

import java.util.List;

public interface CertificateDao extends BaseDao<CertificateData> {
    CertificateData update(CertificateData certificate) throws DaoException;

    List<CertificateData> findByFilter(CertificateFilter filter, PageData page) throws DaoException;
}
