package com.epam.esm.model.dao;

import com.epam.esm.model.dto.CertificateData;
import com.epam.esm.model.dto.CertificateFilter;
import com.epam.esm.model.dto.CertificateSort;
import com.epam.esm.model.dto.PageData;
import com.epam.esm.model.exception.DaoException;

import java.util.List;
import java.util.Set;

public interface CertificateDao extends BaseDao<CertificateData> {
    CertificateData update(CertificateData certificate) throws DaoException;

    List<CertificateData> findByFilter(CertificateFilter filter, PageData page, Set<CertificateSort> sorts) throws DaoException;
}
