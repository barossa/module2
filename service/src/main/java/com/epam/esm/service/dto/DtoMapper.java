package com.epam.esm.service.dto;

import com.epam.esm.model.dto.CertificateData;
import com.epam.esm.model.dto.TagData;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class DtoMapper {

    private DtoMapper() {
    }

    public static CertificateDto mapCertificateFromData(CertificateData data) {
        Set<TagData> tagsData = data.getTags();
        Set<TagDto> tagsDto = null;
        if (tagsData != null) {
            tagsDto = tagsData.stream()
                    .map(DtoMapper::mapTagFromData)
                    .collect(Collectors.toSet());
        }
        return new CertificateDto(data.getId(), data.getName(), data.getDescription(), data.getPrice(),
                data.getDuration(), data.getCreateDate(), data.getLastUpdateDate(), tagsDto);
    }

    public static TagDto mapTagFromData(TagData data) {
        Set<CertificateData> certificatesData = data.getCertificates();
        Set<CertificateDto> certificatesDto = null;
        if (certificatesData != null) {
            certificatesDto = certificatesData.stream()
                    .map(DtoMapper::mapCertificateFromData)
                    .collect(Collectors.toSet());
        }
        return new TagDto(data.getId(), data.getName(), certificatesDto);
    }

    public static <T extends Collection<CertificateDto>> T mapCertificatesFromData(Collection<CertificateData> data,
                                                                                   Collector<CertificateDto, ?, T> collector) {
        T certificateDtos = null;
        if (data != null) {
            certificateDtos = data.stream()
                    .map(DtoMapper::mapCertificateFromData)
                    .collect(collector);
        }
        return certificateDtos;
    }

    public static <T extends Collection<TagDto>> T mapTagsFromData(Collection<TagData> data, Collector<TagDto, ?, T> collector) {
        T tagDtos = null;
        if (data != null) {
            tagDtos = data.stream()
                    .map(DtoMapper::mapTagFromData)
                    .collect(collector);
        }
        return tagDtos;
    }

    public static CertificateData mapCertificateToData(CertificateDto dto) {
        Set<TagDto> tagDtos = dto.getTags();
        Set<TagData> tagsData = null;
        if (tagDtos != null) {
            tagsData = dto.getTags().stream()
                    .map(DtoMapper::mapTagToData)
                    .collect(Collectors.toSet());
        }
        return new CertificateData(dto.getId(), dto.getName(), dto.getDescription(), dto.getPrice(),
                dto.getDuration(), dto.getCreateDate(), dto.getLastUpdateDate(), tagsData);
    }

    public static TagData mapTagToData(TagDto dto) {
        Set<CertificateDto> certificateDtos = dto.getCertificates();
        Set<CertificateData> tagsData = null;
        if (certificateDtos != null) {
            tagsData = certificateDtos.stream()
                    .map(DtoMapper::mapCertificateToData)
                    .collect(Collectors.toSet());
        }
        return new TagData(dto.getId(), dto.getName(), tagsData);
    }

    public static <T extends Collection<CertificateData>> T mapCertificatesToData(Collection<CertificateDto> dtos,
                                                                                  Collector<CertificateData, ?, T> collector) {
        T certificatesData = null;
        if (dtos != null) {
            certificatesData = dtos.stream()
                    .map(DtoMapper::mapCertificateToData)
                    .collect(collector);
        }
        return certificatesData;
    }

    public static <T extends Collection<TagData>> T mapTagsToData(Collection<TagDto> dtos,
                                                                  Collector<TagData, ?, T> collector) {
        T tagsData = null;
        if (dtos != null) {
            tagsData = dtos.stream()
                    .map(DtoMapper::mapTagToData)
                    .collect(collector);
        }
        return tagsData;
    }

}
