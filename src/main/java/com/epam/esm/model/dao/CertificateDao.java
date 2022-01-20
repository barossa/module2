package com.epam.esm.model.dao;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.exception.DaoException;

import java.util.List;

public interface CertificateDao extends BaseDao<Certificate> {
    int update(Certificate certificate) throws DaoException;

    List<Certificate> findByTagId(int id) throws DaoException;
}
