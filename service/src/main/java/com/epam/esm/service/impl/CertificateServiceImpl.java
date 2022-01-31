package com.epam.esm.service.impl;

import com.epam.esm.model.dao.CertificateDao;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.dto.CertificateData;
import com.epam.esm.model.dto.TagData;
import com.epam.esm.model.exception.DaoException;
import com.epam.esm.model.util.EntityUtils;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.DtoMapper;
import com.epam.esm.service.dto.Filter;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.extend.*;
import com.epam.esm.service.validator.CertificateValidator;
import com.epam.esm.service.validator.TagValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

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
    public CertificateDto save(CertificateDto certificate) {
        try {
            validateCertificate(certificate);
            Set<TagData> tagsData = DtoMapper.mapTagsToData(certificate.getTags(), Collectors.toSet());
            Set<TagData> savedTagsData = tagDao.saveAll(tagsData);
            Set<TagDto> savedTags = DtoMapper.mapTagsFromData(savedTagsData, Collectors.toSet());
            certificate.setTags(savedTags);

            CertificateData certificateData = DtoMapper.mapCertificateToData(certificate);
            CertificateData savedCertificateData = certificateDao.save(certificateData);
            if (savedCertificateData == null) {
                logger.warn("Certificate data not presented");
                throw new ObjectPostingException();
            }
            return DtoMapper.mapCertificateFromData(savedCertificateData);

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
    public CertificateDto find(int id) {
        try {
            CertificateData certificateData = certificateDao.find(id);
            if (certificateData == null) {
                throw new ObjectNotFoundException();
            }
            Set<TagData> tags = tagDao.findByCertificateId(id);
            certificateData.setTags(tags);
            return DtoMapper.mapCertificateFromData(certificateData);

        } catch (DaoException e) {
            logger.error("Can't find certificate by id", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public List<CertificateDto> findAll() {
        try {
            List<CertificateData> certificatesData = certificateDao.findAll();
            return DtoMapper.mapCertificatesFromData(certificatesData, Collectors.toList());

        } catch (DaoException e) {
            logger.error("Can't find certificates", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    @Transactional
    public int update(CertificateDto certificate) {
        try {
            CertificateData oldCertificateData = certificateDao.find(certificate.getId());
            if (oldCertificateData == null) {
                throw new ObjectNotPresentedForUpdateException();
            }
            CertificateData newCertificateData = DtoMapper.mapCertificateToData(certificate);
            EntityUtils.replaceNullProperties(oldCertificateData, newCertificateData);
            validateCertificate(certificate);

            Set<TagData> tagsData = DtoMapper.mapTagsToData(certificate.getTags(), Collectors.toSet());
            Set<TagData> savedTagsData = tagDao.saveAll(tagsData);
            Set<TagDto> savedTags = DtoMapper.mapTagsFromData(savedTagsData, Collectors.toSet());
            certificate.setTags(savedTags);

            CertificateData certificateData = DtoMapper.mapCertificateToData(certificate);
            return certificateDao.update(certificateData);

        } catch (DaoException e) {
            logger.error("Can't update certificate", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public List<CertificateDto> findByFilter(Filter filter) {
        try {
            validateFilter(filter);
            List<CertificateData> certificatesData = certificateDao.findByOptions(filter.getTags(), filter.getNames(), filter.getDescriptions(), filter.isStrong());
            return DtoMapper.mapCertificatesFromData(certificatesData, Collectors.toList());

        } catch (DaoException e) {
            logger.error("Can't find certificates by queries", e);
            throw new DataAccessException(e);
        }
    }

    private void validateCertificate(CertificateDto certificate) {
        List<String> errors = certificateValidator.validate(certificate);
        if (!errors.isEmpty()) {
            throw new ObjectValidationException(errors);
        }
    }

    private void validateFilter(Filter filter) {
        BinaryOperator<List<String>> listCombiner = (a, b) -> {
            b.addAll(a);
            return b;
        };
        initFilter(filter);
        List<String> tagErrors = filter.getTags().stream()
                .map(tagValidator::validateName)
                .reduce(new ArrayList<>(), listCombiner);
        List<String> nameErrors = filter.getNames().stream()
                .map(certificateValidator::validateName)
                .reduce(new ArrayList<>(), listCombiner);
        List<String> descriptionErrors = filter.getDescriptions().stream()
                .map(certificateValidator::validateDescription)
                .reduce(new ArrayList<>(), listCombiner);
        tagErrors.addAll(nameErrors);
        tagErrors.addAll(descriptionErrors);
        if (!tagErrors.isEmpty()) {
            throw new ObjectValidationException(tagErrors);
        }
    }

    private void initFilter(Filter filter) {
        if (filter.getNames() == null) {
            filter.setNames(Collections.emptyList());
        }
        if (filter.getTags() == null) {
            filter.setTags(Collections.emptyList());
        }
        if (filter.getDescriptions() == null) {
            filter.setDescriptions(Collections.emptyList());
        }
    }
}
