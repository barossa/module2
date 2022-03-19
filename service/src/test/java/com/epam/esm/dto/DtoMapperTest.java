package com.epam.esm.dto;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.DtoMapper;
import com.epam.esm.dto.TagDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

class DtoMapperTest {
    private Certificate certificateData;
    private CertificateDto certificateDto;
    private Tag tagData;
    private TagDto tagDto;

    @BeforeEach
    public void setUp() {
        LocalDateTime now = LocalDateTime.now();
        certificateData = new Certificate(1, "name", "description", new BigDecimal(100), 10L, now, now, Collections.EMPTY_SET);
        certificateDto = new CertificateDto(1, "name", "description", new BigDecimal(100), 10L, now, now, Collections.EMPTY_SET);
        tagData = new Tag(1, "tag", Collections.EMPTY_SET);
        tagDto = new TagDto(1, "tag", Collections.EMPTY_SET);
    }

    @Test
    void mapCertificateFromDataTest() {
        CertificateDto actualDto = DtoMapper.mapCertificateFromData(certificateData);
        Assertions.assertEquals(certificateDto, actualDto);
    }

    @Test
    void mapCertificateToDataTest() {
        Certificate actualData = DtoMapper.mapCertificateToData(certificateDto);
        Assertions.assertEquals(certificateData, actualData);
    }

    @Test
    void mapTagFromDataTest() {
        TagDto actualDto = DtoMapper.mapTagFromData(tagData);
        Assertions.assertEquals(tagDto, actualDto);
    }

    @Test
    void mapTagToDataTest() {
        Tag actualData = DtoMapper.mapTagToData(tagDto);
        Assertions.assertEquals(tagData, actualData);
    }

}
