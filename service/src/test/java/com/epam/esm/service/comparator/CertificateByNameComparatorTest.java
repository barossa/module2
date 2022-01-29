package com.epam.esm.service.comparator;

import com.epam.esm.service.dto.CertificateDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CertificateByNameComparatorTest {
    private CertificateDto firstCertificateDto;
    private CertificateDto secondCertificateDto;

    private List<CertificateDto> certificatesDto;

    private Comparator<CertificateDto> comparator;

    @BeforeEach
    public void setUp() {
        firstCertificateDto = new CertificateDto();
        firstCertificateDto.setName("Abc certificate");

        secondCertificateDto = new CertificateDto();
        secondCertificateDto.setName("Zero certificate");

        certificatesDto = new ArrayList<>();
        certificatesDto.add(secondCertificateDto);
        certificatesDto.add(firstCertificateDto);

        comparator = new CertificateByNameComparator();
    }

    @Test
    public void compareTest() {
        certificatesDto.sort(comparator);
        CertificateDto actualDto = certificatesDto.get(0);
        Assertions.assertEquals(firstCertificateDto, actualDto);
    }

    @Test
    public void reverseCompareTest() {
        certificatesDto.sort(comparator.reversed());
        CertificateDto actualDto = certificatesDto.get(0);
        Assertions.assertEquals(secondCertificateDto, actualDto);
    }
}
