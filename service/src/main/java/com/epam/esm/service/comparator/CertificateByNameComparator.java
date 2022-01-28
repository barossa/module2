package com.epam.esm.service.comparator;

import com.epam.esm.service.dto.CertificateDto;

import java.util.Comparator;

public class CertificateByNameComparator implements Comparator<CertificateDto> {
    @Override
    public int compare(CertificateDto o1, CertificateDto o2) {
        return String.CASE_INSENSITIVE_ORDER.compare(o1.getName(), o2.getName());
    }
}
