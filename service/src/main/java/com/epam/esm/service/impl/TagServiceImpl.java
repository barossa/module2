package com.epam.esm.service.impl;

import com.epam.esm.model.dao.CertificateDao;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.dto.CertificateData;
import com.epam.esm.model.dto.TagData;
import com.epam.esm.model.exception.DaoException;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.DtoMapper;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.extend.*;
import com.epam.esm.service.validator.TagValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {
    private static final Logger logger = LogManager.getLogger(TagServiceImpl.class);

    private final TagDao tagDao;
    private final CertificateDao certificateDao;
    private final TagValidator tagValidator;

    @Autowired
    public TagServiceImpl(TagDao tagDao, CertificateDao certificateDao, TagValidator tagValidator) {
        this.tagDao = tagDao;
        this.certificateDao = certificateDao;
        this.tagValidator = tagValidator;
    }

    @Override
    public TagDto find(int id) {
        try {
            TagData tagData = tagDao.find(id);
            if (tagData == null) {
                throw new ObjectNotFoundException();
            }
            List<CertificateData> certificates = certificateDao.findByTagId(id);
            tagData.setCertificates(new HashSet<>(certificates));
            return DtoMapper.mapTagFromData(tagData);

        } catch (DaoException e) {
            logger.error("Can't find tag", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public List<TagDto> findAll() {
        try {
            List<TagData> tagsData = tagDao.findAll();
            return DtoMapper.mapTagsFromData(tagsData, Collectors.toList());
        } catch (DaoException e) {
            logger.error("Can't find all tags", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public TagDto save(TagDto tag) {
        try {
            validateTag(tag);
            TagData tagByName = tagDao.findByName(tag.getName());
            if (tagByName != null) {
                throw new ObjectAlreadyExistsException();
            }

            TagData dataToSave = DtoMapper.mapTagToData(tag);
            TagData savedTagData = tagDao.save(dataToSave);
            if (savedTagData == null) {
                logger.warn("Saved tag is not presented: {}", tag);
                throw new ObjectPostingException();
            }
            return DtoMapper.mapTagFromData(savedTagData);

        } catch (DaoException e) {
            logger.error("Can't save tag", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public int delete(int id) {
        try {
            return tagDao.delete(id);
        } catch (DaoException e) {
            logger.error("Can't delete tag", e);
            throw new DataAccessException(e);
        }
    }

    private void validateTag(TagDto tag) {
        List<String> errors = tagValidator.validateName(tag.getName());
        if (!errors.isEmpty()) {
            throw new ObjectValidationException(errors);
        }
    }
}
