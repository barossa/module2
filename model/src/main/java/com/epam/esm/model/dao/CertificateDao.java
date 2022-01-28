package com.epam.esm.model.dao;

import com.epam.esm.model.dto.CertificateData;
import com.epam.esm.model.exception.DaoException;

import java.util.List;

public interface CertificateDao extends BaseDao<CertificateData> {
    int update(CertificateData certificate) throws DaoException;

    List<CertificateData> findByOptions(List<String> tags, List<String> names, List<String> descriptions, boolean strong) throws DaoException;

    List<CertificateData> findByTagId(int id) throws DaoException;
}
