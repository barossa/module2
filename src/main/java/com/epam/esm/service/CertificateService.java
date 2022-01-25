package com.epam.esm.service;

import com.epam.esm.model.entity.Certificate;

import java.util.List;

public interface CertificateService extends BaseService<Certificate> {
    int update(Certificate certificate);

    List<Certificate> findByTagName(String name);

    List<Certificate> findByPartOfNameOrDescription(String[] searches);
}
