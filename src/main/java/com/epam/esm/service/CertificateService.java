package com.epam.esm.service;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.exception.ServiceException;

import java.util.Optional;

public interface CertificateService {
    Optional<Certificate> save(Certificate certificate) throws ServiceException;
    Optional<Certificate> find(int id) throws ServiceException;
}
