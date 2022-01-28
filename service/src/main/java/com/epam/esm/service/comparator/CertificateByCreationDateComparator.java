package com.epam.esm.service.comparator;

import com.epam.esm.service.dto.CertificateDto;

import java.util.Comparator;

public class CertificateByCreationDateComparator implements Comparator<CertificateDto> {
    private static final int EQUALS = 0;
    private static final int LEFT_BIGGER = -1;
    private static final int RIGHT_BIGGER = 1;

    @Override
    public int compare(CertificateDto o1, CertificateDto o2) {
        int result = o1.getCreateDate().compareTo(o2.getCreateDate());
        if (result == EQUALS) {
            return EQUALS;
        }
        if (result > 0) {
            return RIGHT_BIGGER;
        } else {
            return LEFT_BIGGER;
        }
    }

    @Override
    public Comparator<CertificateDto> reversed() {
        return Comparator.super.reversed();
    }
}
