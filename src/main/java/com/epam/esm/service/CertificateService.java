package com.epam.esm.service;

import com.epam.esm.model.entity.Certificate;

public interface CertificateService extends BaseService<Certificate> {
    int update(Certificate certificate);
}
