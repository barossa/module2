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
        Set<TagDto> tagsDto = data.getTags().stream()
                .map(DtoMapper::mapTagFromData)
                .collect(Collectors.toSet());
        return new CertificateDto(data.getId(), data.getName(), data.getDescription(), data.getPrice(),
                data.getDuration(), data.getCreateDate(), data.getLastUpdateDate(), tagsDto);
    }

    public static TagDto mapTagFromData(TagData data) {
        Set<CertificateDto> certificatesDto = data.getCertificates().stream()
                .map(DtoMapper::mapCertificateFromData)
                .collect(Collectors.toSet());
        return new TagDto(data.getId(), data.getName(), certificatesDto);
    }

    public static <T extends Collection<CertificateDto>> T mapCertificatesFromData(Collection<CertificateData> data,
                                                                                   Collector<CertificateDto, ?, T> collector) {
        return data.stream()
                .map(DtoMapper::mapCertificateFromData)
                .collect(collector);
    }

    public static <T extends Collection<TagDto>> T mapTagsFromData(Collection<TagData> data, Collector<TagDto, ?, T> collector) {
        return data.stream()
                .map(DtoMapper::mapTagFromData)
                .collect(collector);
    }

    public static CertificateData mapCertificateToData(CertificateDto dto) {
        Set<TagData> tagsData = dto.getTags().stream()
                .map(DtoMapper::mapTagToData)
                .collect(Collectors.toSet());
        return new CertificateData(dto.getId(), dto.getName(), dto.getDescription(), dto.getPrice(),
                dto.getDuration(), dto.getCreateDate(), dto.getLastUpdateDate(), tagsData);
    }

    public static TagData mapTagToData(TagDto dto) {
        Set<CertificateData> tagsData = dto.getCertificates().stream()
                .map(DtoMapper::mapCertificateToData)
                .collect(Collectors.toSet());
        return new TagData(dto.getId(), dto.getName(), tagsData);
    }

    public static <T extends Collection<CertificateData>> T mapCertificatesToData(Collection<CertificateDto> dtos,
                                                                                  Collector<CertificateData, ?, T> collector) {
        return dtos.stream()
                .map(DtoMapper::mapCertificateToData)
                .collect(collector);
    }

    public static <T extends Collection<TagData>> T mapTagsToData(Collection<TagDto> dtos,
                                                                  Collector<TagData, ?, T> collector) {
        return dtos.stream()
                .map(DtoMapper::mapTagToData)
                .collect(collector);
    }

}
