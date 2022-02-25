package com.epam.esm.dao;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificateFilter;
import com.epam.esm.entity.CertificateSort;
import com.epam.esm.entity.Page;
import com.epam.esm.exception.DaoException;

import java.util.List;
import java.util.Set;

public interface CertificateDao extends BaseDao<Certificate> {
    Certificate update(Certificate certificate) throws DaoException;

    List<Certificate> findByFilter(CertificateFilter filter, Page page, Set<CertificateSort> sorts) throws DaoException;
}
