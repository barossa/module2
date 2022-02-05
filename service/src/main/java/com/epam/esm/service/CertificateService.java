package com.epam.esm.service;

import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.Filter;

import java.util.List;

public interface CertificateService extends BaseService<CertificateDto> {
    int update(CertificateDto certificate);

    List<CertificateDto> findByFilter(Filter filter);
}
