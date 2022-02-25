package com.epam.esm.service.impl;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificateFilterDto;
import com.epam.esm.dto.PageDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CertificateServiceImplTest {
    private CertificateDto certificateDto;
    private List<CertificateDto> certificateDtos;
    private CertificateFilterDto filter;

    @Mock
    private CertificateServiceImpl certificateService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(certificateService);
        certificateDto = new CertificateDto(1, "Certificate", "Description", new BigDecimal(120), 7L,
                LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet());
        certificateDtos = new ArrayList<>();
        certificateDtos.add(certificateDto);
        filter = new CertificateFilterDto(Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    @Test
    public void findTest() {
        when(certificateService.find(anyInt())).thenReturn(certificateDto);
        CertificateDto actualCertificate = certificateService.find(1);
        Assertions.assertEquals(certificateDto, actualCertificate);
    }

    @Test
    public void findAllTest() {
        when(certificateService.findAll(any(PageDto.class))).thenReturn(certificateDtos);
        List<CertificateDto> actualCertificates = certificateService.findAll(new PageDto());
        Assertions.assertEquals(certificateDtos, actualCertificates);
    }

    @Test
    public void saveTest() {
        when(certificateService.save(any(CertificateDto.class))).thenReturn(certificateDto);
        CertificateDto savedCertificate = certificateService.save(certificateDto);
        Assertions.assertEquals(certificateDto, savedCertificate);
    }

    @Test
    public void deleteTest() {
        when(certificateService.delete(anyInt())).thenReturn(certificateDto);
        CertificateDto actualCertificate = certificateService.delete(1);
        Assertions.assertNotEquals(actualCertificate.getId(), 0);
    }

    @Test
    public void updateTest() {
        when(certificateService.update(certificateDto)).thenReturn(certificateDto);
        CertificateDto updated = certificateService.update(certificateDto);
        Assertions.assertEquals(certificateDto, updated);
    }

    @Test
    public void findByFilterTest() {
        when(certificateService.findByFilter(any(CertificateFilterDto.class), any(PageDto.class), any(Set.class))).thenReturn(certificateDtos);
        Set<String> sorts = new HashSet<>();
        List<CertificateDto> actualCertificates = certificateService.findByFilter(filter, new PageDto(), sorts);
        Assertions.assertEquals(certificateDtos, actualCertificates);
    }

}
