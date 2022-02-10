package com.epam.esm.service.impl;

import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.dto.PageData;
import com.epam.esm.model.dto.TagData;
import com.epam.esm.model.exception.DaoException;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.DtoMapper;
import com.epam.esm.service.dto.PageDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.extend.*;
import com.epam.esm.service.validator.PageValidator;
import com.epam.esm.service.validator.TagValidator;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private static final Logger logger = LogManager.getLogger(TagServiceImpl.class);

    private final TagDao tagDao;
    private final TagValidator tagValidator;
    private final PageValidator pageValidator;

    @Override
    public TagDto find(int id) {
        try {
            TagData tagData = tagDao.find(id);
            if (tagData == null) {
                throw new ObjectNotFoundException();
            }
            return DtoMapper.mapTagFromData(tagData);
        } catch (DaoException e) {
            logger.error("Can't find tag", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public List<TagDto> findAll(PageDto page) {
        try {
            validatePage(page);
            PageData pageData = DtoMapper.mapPageToData(page);
            List<TagData> tagsData = tagDao.findAll(pageData);
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
    public TagDto delete(int id) {
        try {
            TagData tagData = tagDao.find(id);
            if (tagData == null) {
                throw new ObjectNotPresentedForDelete();
            }

            int affectedObjects = tagDao.delete(id);
            if (affectedObjects == 0) {
                throw new ObjectDeletionException();
            }

            return DtoMapper.mapTagFromData(tagData);
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

    private void validatePage(PageDto page) {
        List<String> errors = pageValidator.validatePage(page);
        if (!errors.isEmpty()) {
            throw new ObjectValidationException(errors);
        }
    }
}
