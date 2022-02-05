package com.epam.esm.service.dto;

import com.epam.esm.service.comparator.CertificateByCreationDateComparator;
import com.epam.esm.service.comparator.CertificateByNameComparator;

import java.util.Comparator;

public enum CertificateSort {
    NAME_ASC(new CertificateByNameComparator(), false),
    NAME_DESC(new CertificateByNameComparator(), true),
    DATE_ASC(new CertificateByCreationDateComparator(), false),
    DATE_DESC(new CertificateByCreationDateComparator(), true);

    private final Comparator<CertificateDto> comparator;
    private final boolean reverse;

    CertificateSort(Comparator<CertificateDto> comparator, boolean reverse) {
        this.reverse = reverse;
        this.comparator = comparator;
    }

    public Comparator<CertificateDto> getComparator() {
        return comparator;
    }

    public boolean isReverse() {
        return reverse;
    }
}
