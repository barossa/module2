package com.epam.esm.service.impl;

import com.epam.esm.controller.exception.extend.DataAccessException;
import com.epam.esm.controller.exception.extend.ObjectAlreadyExistsException;
import com.epam.esm.controller.exception.extend.ObjectNotFoundException;
import com.epam.esm.controller.exception.extend.ObjectPostingException;
import com.epam.esm.model.dao.CertificateDao;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.exception.DaoException;
import com.epam.esm.model.exception.ServiceException;
import com.epam.esm.service.TagService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    private static final Logger logger = LogManager.getLogger(TagServiceImpl.class);

    private final TagDao tagDao;
    private final CertificateDao certificateDao;

    public TagServiceImpl(TagDao tagDao, CertificateDao certificateDao) {
        this.tagDao = tagDao;
        this.certificateDao = certificateDao;
    }

    @Override
    public Tag find(int id) throws ServiceException {
        try {
            Tag tag = tagDao.find(id);
            if (tag == null) {
                throw new ObjectNotFoundException();
            }
            List<Certificate> certificates = certificateDao.findByTagId(id);
            tag.setCertificates(new HashSet<>(certificates));
            return tag;

        } catch (DaoException e) {
            logger.error("Can't find tag", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public List<Tag> findAll() throws ServiceException {
        try {
            return tagDao.findAll();
        } catch (DaoException e) {
            logger.error("Can't find all tags", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public Tag save(Tag tag) throws ServiceException {
        try {
            Tag tagByName = tagDao.findByName(tag.getName());
            if (tagByName != null) {
                throw new ObjectAlreadyExistsException();
            }

            Tag savedTag = tagDao.save(tag);
            if (savedTag == null) {
                logger.warn("Saved tag is not presented: {}", tag);
                throw new ObjectPostingException();
            }
            return savedTag;

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
}
