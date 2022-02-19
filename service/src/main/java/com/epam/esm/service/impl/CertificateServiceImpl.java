package com.epam.esm.service.impl;

import com.epam.esm.model.dao.CertificateDao;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.dto.*;
import com.epam.esm.model.exception.DaoException;
import com.epam.esm.model.util.EntityUtils;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.DtoMapper;
import com.epam.esm.service.dto.Filter;
import com.epam.esm.service.dto.PageDto;
import com.epam.esm.service.exception.extend.*;
import com.epam.esm.service.validator.CertificateValidator;
import com.epam.esm.service.validator.PageValidator;
import com.epam.esm.service.validator.TagValidator;
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
            Set<TagData> tagsData = DtoMapper.mapTagsToData(certificate.getTags(), Collectors.toSet());
            Set<TagData> savedTagsData = tagDao.saveAll(tagsData);

            CertificateData certificateData = DtoMapper.mapCertificateToData(certificate);
            certificateData.setTags(savedTagsData);
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
    @Transactional
    public CertificateDto delete(int id) {
        try {
            CertificateData certificateData = certificateDao.find(id);
            if (certificateData == null) {
                throw new ObjectNotPresentedForDelete();
            }

            int affectedObjects = certificateDao.delete(id);
            if (affectedObjects == 0) {
                throw new ObjectDeletionException();
            }

            return DtoMapper.mapCertificateFromData(certificateData);
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
                throw new CertificateNotFoundException();
            }
            return DtoMapper.mapCertificateFromData(certificateData);

        } catch (DaoException e) {
            logger.error("Can't find certificate by id", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public List<CertificateDto> findAll(PageDto page) {
        try {
            validatePage(page);
            PageData pageData = DtoMapper.mapPageToData(page);
            List<CertificateData> certificatesData = certificateDao.findAll(pageData);
            return DtoMapper.mapCertificatesFromData(certificatesData, Collectors.toList());

        } catch (DaoException e) {
            logger.error("Can't find certificates", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    @Transactional
    public CertificateDto update(CertificateDto certificate) {
        try {
            CertificateData oldCertificateData = certificateDao.find(certificate.getId());
            if (oldCertificateData == null) {
                throw new ObjectNotPresentedForUpdateException();
            }
            //Validating new certificate data
            CertificateData newCertificateData = DtoMapper.mapCertificateToData(certificate);
            EntityUtils.replaceNullProperties(oldCertificateData, newCertificateData);
            validateCertificate(newCertificateData);

            //Merging new certificate data via JPA
            Set<TagData> savedTagsData = tagDao.saveAll(newCertificateData.getTags());
            newCertificateData.setTags(savedTagsData);
            BeanUtils.copyProperties(newCertificateData, oldCertificateData);

            return DtoMapper.mapCertificateFromData(oldCertificateData);

        } catch (DaoException e) {
            logger.error("Can't update certificate", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    @Transactional()
    public List<CertificateDto> findByFilter(Filter filter, PageDto page, Set<String> sorts) {
        try {
            validateFilter(filter);
            validatePage(page);
            Set<CertificateSort> validateSorts = validateSorts(sorts);
            List<String> tagNames = filter.getTags();
            List<TagData> tagsByName = tagDao.findByNames(tagNames);
            List<CertificateDto> certificatesDto = new ArrayList<>();
            if (tagNames.size() == tagsByName.size()) {
                CertificateFilter certificateFilter = DtoMapper.mapFilter(filter);
                certificateFilter.setTags(tagsByName);
                PageData pageData = DtoMapper.mapPageToData(page);

                List<CertificateData> certificatesData = certificateDao.findByFilter(certificateFilter, pageData, validateSorts);
                certificatesDto = DtoMapper.mapCertificatesFromData(certificatesData, Collectors.toList());
            }
            return certificatesDto;

        } catch (DaoException e) {
            logger.error("Can't find certificates by filter", e);
            throw new DataAccessException(e);
        }
    }

    private void validateCertificate(CertificateData data) {
        CertificateDto dto = DtoMapper.mapCertificateFromData(data);
        validateCertificate(dto);
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

    private void validatePage(PageDto page) {
        if (page == null) {
            page = new PageDto();
        }
        List<String> errors = pageValidator.validatePage(page);
        if (!errors.isEmpty()) {
            throw new ObjectValidationException(errors);
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
