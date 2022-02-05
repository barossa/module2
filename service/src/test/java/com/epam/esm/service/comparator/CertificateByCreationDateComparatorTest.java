package com.epam.esm.service.comparator;

import com.epam.esm.service.dto.CertificateDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CertificateByCreationDateComparatorTest {

    private CertificateDto oldestCertificateDto;
    private CertificateDto newestCertificateDto;

    private List<CertificateDto> certificatesDto;

    private Comparator<CertificateDto> comparator;

    @BeforeEach
    public void setUp(){
        oldestCertificateDto = new CertificateDto();
        oldestCertificateDto.setCreateDate(LocalDateTime.of(2000, 1,1,1,1));

        newestCertificateDto = new CertificateDto();
        newestCertificateDto.setCreateDate(LocalDateTime.of(2022, 1,1,1,1));

        certificatesDto = new ArrayList<>();
        certificatesDto.add(newestCertificateDto);
        certificatesDto.add(oldestCertificateDto);

        comparator = new CertificateByCreationDateComparator();
    }

    @Test
    public void compareTest(){
        certificatesDto.sort(comparator);
        CertificateDto actualDto = certificatesDto.get(0);
        Assertions.assertEquals(oldestCertificateDto, actualDto);
    }

    @Test
    public void reverseCompareTest(){
        certificatesDto.sort(comparator.reversed());
        CertificateDto actualDto = certificatesDto.get(0);
        Assertions.assertEquals(newestCertificateDto, actualDto);
    }
}
