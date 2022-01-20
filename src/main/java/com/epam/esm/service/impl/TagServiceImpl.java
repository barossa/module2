package com.epam.esm.service.impl;

import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.exception.DaoException;
import com.epam.esm.model.exception.ServiceException;
import com.epam.esm.service.TagService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;

    public TagServiceImpl(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public Optional<Tag> find(int id) throws ServiceException {
        try {
            Tag tag = tagDao.find(id);
            return Optional.ofNullable(tag);
        } catch (DaoException e) {
            throw new ServiceException("Can't find tag by id", e);
        }
    }

    @Override
    public List<Tag> findAll() throws ServiceException {
        try {
            return tagDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException("Can't find tags", e);
        }
    }

    @Override
    public Optional<Tag> save(Tag tag) throws ServiceException {
        try {
            Tag savedTag = tagDao.save(tag);
            return Optional.ofNullable(savedTag);
        } catch (DaoException e) {
            throw new ServiceException("Can't save tag", e);
        }
    }

    @Override
    public Optional<Tag> findByName(String name) throws ServiceException {
        try {
            Tag savedTag = tagDao.findByName(name);
            return Optional.ofNullable(savedTag);
        } catch (DaoException e) {
            throw new ServiceException("Can't find tag by name", e);
        }
    }
}
