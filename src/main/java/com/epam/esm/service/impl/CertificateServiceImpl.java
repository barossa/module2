package com.epam.esm.service.impl;

import com.epam.esm.model.dao.CertificateDao;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.exception.DaoException;
import com.epam.esm.model.exception.ServiceException;
import com.epam.esm.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CertificateServiceImpl implements CertificateService {
    private final CertificateDao certificateDao;
    private final TagDao tagDao;

    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao, TagDao tagDao) {
        this.certificateDao = certificateDao;
        this.tagDao = tagDao;
    }

    @Override
    public Optional<Certificate> save(Certificate certificate) throws ServiceException {
        try {
            LocalDateTime now = LocalDateTime.now();
            certificate.setLastUpdateDate(now);
            certificate.setCreateDate(now);
            Certificate savedCertificate = certificateDao.save(certificate);
            return Optional.ofNullable(savedCertificate);
        } catch (DaoException e) {
            throw new ServiceException("Error occurred saving certificate", e);
        }
    }

    @Override
    public Optional<Certificate> find(int id) throws ServiceException {
        try {
            Certificate certificate = certificateDao.find(id);
            return Optional.ofNullable(certificate);
        } catch (DaoException e) {
            throw new ServiceException("Can't find certificate by id", e);
        }
    }

    @Override
    public List<Certificate> findAll() throws ServiceException {
        try{
            return certificateDao.findAll();
        }catch (DaoException e){
            throw new ServiceException("Can't find certificates");
        }
    }

    @Override
    public int update(Certificate certificate) throws ServiceException {
        try{
            return certificateDao.update(certificate);
        }catch (DaoException e){
            throw new ServiceException("Can't update certificate");
        }
    }
}
