package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.Page;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.extend.*;
import com.epam.esm.service.TagService;
import com.epam.esm.dto.DtoMapper;
import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.validator.PageValidator;
import com.epam.esm.validator.TagValidator;
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
    private final UserDao userDao;
    private final TagValidator tagValidator;
    private final PageValidator pageValidator;

    @Override
    public TagDto find(int id) {
        try {
            Tag tagData = tagDao.find(id);
            if (tagData == null) {
                throw new TagNotFoundException();
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
            Page pageData = DtoMapper.mapPageToData(page);
            List<Tag> tagsData = tagDao.findAll(pageData);
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
            Tag tagByName = tagDao.findByName(tag.getName());
            if (tagByName != null) {
                throw new ObjectAlreadyExistsException();
            }

            Tag dataToSave = DtoMapper.mapTagToData(tag);
            Tag savedTagData = tagDao.save(dataToSave);
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
            Tag tagData = tagDao.find(id);
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

    @Override
    public TagDto findMostUsedOfUser(int userId) {
        try {
            User userData = userDao.find(userId);
            if (userData == null) {
                throw new UserNotFoundException();
            }
            Tag tagData = tagDao.findMostUsedOfUser(userData);
            if (tagData == null) {
                throw new TagNotFoundException();
            }

            return DtoMapper.mapTagFromData(tagData);

        } catch (DaoException e) {
            logger.error("Can't find most used tag of user by id", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public TagDto findMostUsedOfTopUser() {
        try {
            Tag tagData = tagDao.findMostUsedOfTopUser();
            if (tagData == null) {
                throw new TagNotFoundException();
            }
            return DtoMapper.mapTagFromData(tagData);

        } catch (DaoException e) {
            logger.error("Can't find most used tag of top user", e);
            throw new DataAccessException(e);
        }
    }
}
