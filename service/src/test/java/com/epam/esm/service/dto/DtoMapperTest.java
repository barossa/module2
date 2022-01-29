package com.epam.esm.service.dto;

import com.epam.esm.model.dto.CertificateData;
import com.epam.esm.model.dto.TagData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

public class DtoMapperTest {
    private CertificateData certificateData;
    private CertificateDto certificateDto;
    private TagData tagData;
    private TagDto tagDto;

    @BeforeEach
    public void setUp() {
        LocalDateTime now = LocalDateTime.now();
        certificateData = new CertificateData(1, "name", "description", new BigDecimal(100), 10L, now, now, Collections.EMPTY_SET);
        certificateDto = new CertificateDto(1, "name", "description", new BigDecimal(100), 10L, now, now, Collections.EMPTY_SET);
        tagData = new TagData(1, "tag", Collections.EMPTY_SET);
        tagDto = new TagDto(1, "tag", Collections.EMPTY_SET);
    }

    @Test
    public void mapCertificateFromDataTest() {
        CertificateDto actualDto = DtoMapper.mapCertificateFromData(certificateData);
        Assertions.assertEquals(certificateDto, actualDto);
    }

    @Test
    public void mapCertificateToDataTest() {
        CertificateData actualData = DtoMapper.mapCertificateToData(certificateDto);
        Assertions.assertEquals(certificateData, actualData);
    }

    @Test
    public void mapTagFromDataTest() {
        TagDto actualDto = DtoMapper.mapTagFromData(tagData);
        Assertions.assertEquals(tagDto, actualDto);
    }

    @Test
    public void mapTagToDataTest() {
        TagData actualData = DtoMapper.mapTagToData(tagDto);
        Assertions.assertEquals(tagData, actualData);
    }

}
