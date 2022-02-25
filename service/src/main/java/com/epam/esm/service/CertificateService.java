package com.epam.esm.service;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificateFilterDto;
import com.epam.esm.dto.PageDto;

import java.util.List;
import java.util.Set;

public interface CertificateService extends BaseService<CertificateDto> {
    CertificateDto update(CertificateDto certificate);

    List<CertificateDto> findByFilter(CertificateFilterDto filter, PageDto page, Set<String> sorts);
}
