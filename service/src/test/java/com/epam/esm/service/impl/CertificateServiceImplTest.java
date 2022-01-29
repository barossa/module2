package com.epam.esm.service.impl;

import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.Filter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CertificateServiceImplTest {
    private CertificateDto certificateDto;
    private List<CertificateDto> certificateDtos;
    private Filter filter;

    @Mock
    private CertificateServiceImpl certificateService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(certificateService);
        certificateDto = new CertificateDto(1, "Certificate", "Description", new BigDecimal(120), 7L,
                LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet());
        certificateDtos = new ArrayList<>();
        certificateDtos.add(certificateDto);
        filter = new Filter(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), true);
    }

    @Test
    public void findTest() {
        when(certificateService.find(anyInt())).thenReturn(certificateDto);
        CertificateDto actualCertificate = certificateService.find(1);
        Assertions.assertEquals(certificateDto, actualCertificate);
    }

    @Test
    public void findAllTest() {
        when(certificateService.findAll()).thenReturn(certificateDtos);
        List<CertificateDto> actualCertificates = certificateService.findAll();
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
        when(certificateService.delete(anyInt())).thenReturn(1);
        int affectedObjects = certificateService.delete(1);
        Assertions.assertNotEquals(affectedObjects, 0);
    }

    @Test
    public void updateTest() {
        when(certificateService.update(any(CertificateDto.class))).thenReturn(1);
        int affectedObjects = certificateService.update(certificateDto);
        Assertions.assertNotEquals(affectedObjects, 0);
    }

    @Test
    public void findByFilterTest() {
        when(certificateService.findByFilter(any(Filter.class))).thenReturn(certificateDtos);
        List<CertificateDto> actualCertificates = certificateService.findByFilter(filter);
        Assertions.assertEquals(certificateDtos, actualCertificates);
    }

}
