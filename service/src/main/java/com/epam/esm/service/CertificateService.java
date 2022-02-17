package com.epam.esm.service;

import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.Filter;
import com.epam.esm.service.dto.PageDto;

import java.util.List;
import java.util.Set;

public interface CertificateService extends BaseService<CertificateDto> {
    CertificateDto update(CertificateDto certificate);

    List<CertificateDto> findByFilter(Filter filter, PageDto page, Set<String> sorts);
}
