package com.epam.esm.service;

import com.epam.esm.service.dto.CertificateDto;

import java.util.List;

public interface CertificateService extends BaseService<CertificateDto> {
    int update(CertificateDto certificate);

    List<CertificateDto> findByTagName(String name);

    List<CertificateDto> findByPartOfNameOrDescription(String[] searches);
}
