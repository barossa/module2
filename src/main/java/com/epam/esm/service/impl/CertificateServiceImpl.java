package com.epam.esm.service.impl;

import com.epam.esm.controller.exception.extend.*;
import com.epam.esm.model.EntityUtils;
import com.epam.esm.model.dao.CertificateDao;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.exception.DaoException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.validator.CertificateValidator;
import com.epam.esm.service.validator.TagValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CertificateServiceImpl implements CertificateService {
    private static final Logger logger = LogManager.getLogger(CertificateServiceImpl.class);

    private final CertificateDao certificateDao;
    private final TagDao tagDao;
    private final CertificateValidator certificateValidator;
    private final TagValidator tagValidator;

    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao, TagDao tagDao,
                                  CertificateValidator certificateValidator, TagValidator tagValidator) {
        this.certificateDao = certificateDao;
        this.tagDao = tagDao;
        this.certificateValidator = certificateValidator;
        this.tagValidator = tagValidator;
    }

    @Override
    @Transactional
    public Certificate save(Certificate certificate) {
        try {
            validateCertificate(certificate);
            Set<Tag> savedTags = tagDao.saveAll(certificate.getTags());
            certificate.setTags(savedTags);
            Certificate savedCertificate = certificateDao.save(certificate);
            if (savedCertificate == null) {
                logger.warn("Certificate not presented");
                throw new ObjectPostingException();
            }
            return savedCertificate;

        } catch (DaoException e) {
            logger.error("Can't save certificate", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public int delete(int id) {
        try {
            return certificateDao.delete(id);
        } catch (DaoException e) {
            logger.error("Can't delete certificate", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public Certificate find(int id) {
        try {
            Certificate certificate = certificateDao.find(id);
            if (certificate == null) {
                throw new ObjectNotFoundException();
            }
            Set<Tag> tags = tagDao.findByCertificateId(id);
            certificate.setTags(tags);
            return certificate;

        } catch (DaoException e) {
            logger.error("Can't find certificate by id", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public List<Certificate> findAll() {
        try {
            return certificateDao.findAll();
        } catch (DaoException e) {
            logger.error("Can't find certificates", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    @Transactional
    public int update(Certificate certificate) {
        try {
            Certificate oldCertificate = certificateDao.find(certificate.getId());
            if (oldCertificate == null) {
                throw new ObjectNotPresentedForUpdateException();
            }
            EntityUtils.replaceNullProperties(oldCertificate, certificate);
            validateCertificate(certificate);
            Set<Tag> savedTags = tagDao.saveAll(certificate.getTags());
            certificate.setTags(savedTags);

            return certificateDao.update(certificate);
        } catch (DaoException e) {
            logger.error("Can't update certificate", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    @Transactional
    public List<Certificate> findByTagName(String name) {
        try {
            List<String> errors = tagValidator.validateName(name);
            if (!errors.isEmpty()) {
                throw new ObjectValidationException(errors);
            }
            Tag tag = tagDao.findByName(name);
            List<Certificate> certificates = new ArrayList<>();
            if (tag != null) {
                certificates = certificateDao.findByTagId(tag.getId());
            }
            return certificates;
        } catch (DaoException e) {
            logger.error("Can't find certificates by tag name", e);
            throw new DataAccessException(e);
        }
    }

    private void validateCertificate(Certificate certificate) {
        List<String> errors = certificateValidator.validate(certificate);
        if (!errors.isEmpty()) {
            throw new ObjectValidationException(errors);
        }
    }
}
