package com.epam.esm.service;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.exception.ServiceException;

public interface CertificateService extends BaseService<Certificate> {
    int update(Certificate certificate) throws ServiceException;
}
