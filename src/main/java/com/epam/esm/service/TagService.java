package com.epam.esm.service;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.exception.ServiceException;

import java.util.Optional;

public interface TagService extends BaseService<Tag> {
    Optional<Tag> findByName(String name) throws ServiceException;
}
