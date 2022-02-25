package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.*;
import com.epam.esm.exception.extend.*;
import com.epam.esm.exception.DaoException;
import com.epam.esm.util.EntityUtils;
import com.epam.esm.service.CertificateService;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.DtoMapper;
import com.epam.esm.dto.CertificateFilterDto;
import com.epam.esm.dto.PageDto;
import com.epam.esm.validator.CertificateValidator;
import com.epam.esm.validator.PageValidator;
import com.epam.esm.validator.TagValidator;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CertificateServiceImpl implements CertificateService {
    private static final Logger logger = LogManager.getLogger(CertificateServiceImpl.class);

    private static final int MAX_SORTS = 2;

    private final CertificateDao certificateDao;
    private final TagDao tagDao;

    private final CertificateValidator certificateValidator;
    private final TagValidator tagValidator;
    private final PageValidator pageValidator;

    @Override
    @Transactional
    public CertificateDto save(CertificateDto certificate) {
        try {
            validateCertificate(certificate);
            Set<Tag> tags = DtoMapper.mapTagsToData(certificate.getTags(), Collectors.toSet());
            Set<Tag> savedTagsData = tagDao.saveAll(tags);

            Certificate certificateData = DtoMapper.mapCertificateToData(certificate);
            certificateData.setTags(savedTagsData);
            Certificate savedCertificateData = certificateDao.save(certificateData);
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
    @Transactional
    public CertificateDto delete(int id) {
        try {
            Certificate certificate = certificateDao.find(id);
            if (certificate == null) {
                throw new ObjectNotPresentedForDelete();
            }
            CertificateDto certificateDto = DtoMapper.mapCertificateFromData(certificate);

            int affectedObjects = certificateDao.delete(id);
            if (affectedObjects == 0) {
                throw new ObjectDeletionException();
            }

            return certificateDto;
        } catch (DaoException e) {
            logger.error("Can't delete certificate", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public CertificateDto find(int id) {
        try {
            Certificate certificate = certificateDao.find(id);
            if (certificate == null) {
                throw new CertificateNotFoundException();
            }
            return DtoMapper.mapCertificateFromData(certificate);

        } catch (DaoException e) {
            logger.error("Can't find certificate by id", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public List<CertificateDto> findAll(PageDto page) {
        try {
            validatePage(page);
            Page pageData = DtoMapper.mapPageToData(page);
            List<Certificate> certificates = certificateDao.findAll(pageData);
            return DtoMapper.mapCertificatesFromData(certificates, Collectors.toList());

        } catch (DaoException e) {
            logger.error("Can't find certificates", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    @Transactional
    public CertificateDto update(CertificateDto certificate) {
        try {
            Certificate oldCertificate = certificateDao.find(certificate.getId());
            if (oldCertificate == null) {
                throw new ObjectNotPresentedForUpdateException();
            }
            //Validating new certificate data
            Certificate newCertificate = DtoMapper.mapCertificateToData(certificate);
            EntityUtils.replaceNullProperties(oldCertificate, newCertificate);
            validateCertificate(newCertificate);

            //Merging new certificate data via JPA
            Set<Tag> savedTagsData = tagDao.saveAll(newCertificate.getTags());
            newCertificate.setTags(savedTagsData);
            BeanUtils.copyProperties(newCertificate, oldCertificate);

            return DtoMapper.mapCertificateFromData(oldCertificate);

        } catch (DaoException e) {
            logger.error("Can't update certificate", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    @Transactional()
    public List<CertificateDto> findByFilter(CertificateFilterDto filter, PageDto page, Set<String> sorts) {
        try {
            validateFilter(filter);
            validatePage(page);
            Set<CertificateSort> validateSorts = validateSorts(sorts);
            List<String> tagNames = filter.getTags();
            List<Tag> tagsByName = tagDao.findByNames(tagNames);
            List<CertificateDto> certificates = new ArrayList<>();
            if (tagNames.size() == tagsByName.size()) {
                CertificateFilter certificateFilter = DtoMapper.mapFilter(filter);
                certificateFilter.setTags(tagsByName);
                Page pageData = DtoMapper.mapPageToData(page);

                List<Certificate> certificatesData = certificateDao.findByFilter(certificateFilter, pageData, validateSorts);
                certificates = DtoMapper.mapCertificatesFromData(certificatesData, Collectors.toList());
            }
            return certificates;

        } catch (DaoException e) {
            logger.error("Can't find certificates by filter", e);
            throw new DataAccessException(e);
        }
    }

    private void validateCertificate(Certificate certificate) {
        CertificateDto dto = DtoMapper.mapCertificateFromData(certificate);
        validateCertificate(dto);
    }

    private void validateCertificate(CertificateDto certificate) {
        List<String> errors = certificateValidator.validate(certificate);
        if (!errors.isEmpty()) {
            throw new ObjectValidationException(errors);
        }
    }

    private void validateFilter(CertificateFilterDto filter) {
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

    private void validatePage(PageDto page) {
        if (page == null) {
            page = new PageDto();
        }
        List<String> errors = pageValidator.validatePage(page);
        if (!errors.isEmpty()) {
            throw new ObjectValidationException(errors);
        }
    }

    private void initFilter(CertificateFilterDto filter) {
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

    private Set<CertificateSort> validateSorts(Set<String> sorts) {
        if (sorts == null) {
            sorts = new HashSet<>();
        }
        if (sorts.size() > MAX_SORTS) {
            throw new InvalidSortParametersException();
        }
        try {
            return sorts.stream()
                    .map(String::toUpperCase)
                    .map(CertificateSort::valueOf)
                    .collect(Collectors.toSet());

        } catch (IllegalArgumentException e) {
            throw new InvalidSortParametersException(e);
        }
    }
}
