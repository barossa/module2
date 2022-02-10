package com.epam.esm.service.dto;

import com.epam.esm.model.dto.*;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class DtoMapper {
    private DtoMapper() {
    }

    public static CertificateFilter mapFilter(Filter filter) {
        CertificateFilter certificateFilter = new CertificateFilter();
        BeanUtils.copyProperties(filter, certificateFilter);
        return certificateFilter;
    }

    public static CertificateDto mapCertificateFromData(CertificateData data) {
        CertificateDto dto = Utils.mapCertificateFromData(data);
        Set<TagData> tagsData = data.getTags();
        Set<TagDto> tagsDto = new HashSet<>();
        if (tagsData != null) {
            tagsDto = tagsData.stream()
                    .map(Utils::mapTagFromData)
                    .collect(Collectors.toSet());
        }
        dto.setTags(tagsDto);
        return dto;
    }

    public static CertificateData mapCertificateToData(CertificateDto dto) {
        CertificateData data = Utils.mapCertificateToData(dto);
        Set<TagDto> tagsDto = dto.getTags();
        Set<TagData> tagsData = new HashSet<>();
        if (tagsDto != null) {
            tagsData = tagsDto.stream()
                    .map(Utils::mapTagToData)
                    .collect(Collectors.toSet());
        }
        data.setTags(tagsData);
        return data;
    }

    public static TagDto mapTagFromData(TagData data) {
        TagDto dto = Utils.mapTagFromData(data);
        Set<CertificateData> certificatesData = data.getCertificates();
        Set<CertificateDto> certificatesDto = new HashSet<>();
        if (certificatesData != null) {
            certificatesDto = certificatesData.stream()
                    .map(Utils::mapCertificateFromData)
                    .collect(Collectors.toSet());
        }
        dto.setCertificates(certificatesDto);
        return dto;
    }

    public static TagData mapTagToData(TagDto dto) {
        TagData data = Utils.mapTagToData(dto);
        Set<CertificateDto> certificatesDto = dto.getCertificates();
        Set<CertificateData> certificatesData = new HashSet<>();
        if (certificatesDto != null) {
            certificatesData = certificatesDto.stream()
                    .map(Utils::mapCertificateToData)
                    .collect(Collectors.toSet());
        }
        data.setCertificates(certificatesData);
        return data;
    }

    public static UserDto mapUserFromData(UserData data) {
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(data, dto);
        return dto;
    }

    public static UserData mapUserToData(UserDto dto) {
        UserData data = new UserData();
        BeanUtils.copyProperties(dto, data);
        return data;
    }

    public static OrderDto mapOrderFromData(OrderData data){
        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(data, orderDto);
        UserDto userDto = DtoMapper.mapUserFromData(data.getUser());
        CertificateDto certificateDto = DtoMapper.mapCertificateFromData(data.getCertificate());
        orderDto.setUser(userDto);
        orderDto.setCertificate(certificateDto);
        return orderDto;
    }

    public static PageData mapPageToData(PageDto dto){
        return new PageData(dto.getPage() * dto.getLimit(), dto.getLimit());
    }

    public static <T extends Collection<UserDto>> T mapUsersFromData(Collection<UserData> data, Collector<UserDto, ?, T> collector) {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data.stream()
                .map(DtoMapper::mapUserFromData)
                .collect(collector);
    }


    public static <T extends Collection<OrderDto>> T mapOrdersFromData(Collection<OrderData> data, Collector<OrderDto, ?, T> collector) {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data.stream()
                .map(DtoMapper::mapOrderFromData)
                .collect(collector);
    }


    public static <T extends Collection<CertificateDto>> T mapCertificatesFromData(Collection<CertificateData> data, Collector<CertificateDto, ?, T> collector) {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data.stream()
                .map(DtoMapper::mapCertificateFromData)
                .collect(collector);
    }

    public static <T extends Collection<TagDto>> T mapTagsFromData(Collection<TagData> data, Collector<TagDto, ?, T> collector) {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data.stream()
                .map(DtoMapper::mapTagFromData)
                .collect(collector);
    }

    public static <T extends Collection<CertificateData>> T mapCertificatesToData(Collection<CertificateDto> dtos,
                                                                                  Collector<CertificateData, ?, T> collector) {
        if (dtos == null) {
            dtos = new ArrayList<>();
        }
        return dtos.stream()
                .map(DtoMapper::mapCertificateToData)
                .collect(collector);
    }

    public static <T extends Collection<TagData>> T mapTagsToData(Collection<TagDto> dtos,
                                                                  Collector<TagData, ?, T> collector) {
        if (dtos == null) {
            dtos = new ArrayList<>();
        }
        return dtos.stream()
                .map(DtoMapper::mapTagToData)
                .collect(collector);
    }

    private static class Utils {

        public static CertificateDto mapCertificateFromData(CertificateData data) {
            CertificateDto dto = new CertificateDto();
            BeanUtils.copyProperties(data, dto);
            return dto;
        }

        public static CertificateData mapCertificateToData(CertificateDto dto) {
            CertificateData data = new CertificateData();
            BeanUtils.copyProperties(dto, data);
            return data;
        }

        public static TagDto mapTagFromData(TagData data) {
            TagDto dto = new TagDto();
            BeanUtils.copyProperties(data, dto);
            return dto;
        }

        public static TagData mapTagToData(TagDto dto) {
            TagData data = new TagData();
            BeanUtils.copyProperties(dto, data);
            return data;
        }

        public static <T extends Collection<CertificateDto>> T mapCertificatesFromData(Collection<CertificateData> data,
                                                                                       Collector<CertificateDto, ?, T> collector) {
            return data.stream()
                    .map(Utils::mapCertificateFromData)
                    .collect(collector);
        }

        public static <T extends Collection<CertificateData>> T mapCertificatesToData(Collection<CertificateDto> dtos,
                                                                                      Collector<CertificateData, ?, T> collector) {
            return dtos.stream()
                    .map(Utils::mapCertificateToData)
                    .collect(collector);
        }

        public static <T extends Collection<TagDto>> T mapTagsFromData(Collection<TagData> data,
                                                                       Collector<TagDto, ?, T> collector) {
            return data.stream()
                    .map(Utils::mapTagFromData)
                    .collect(collector);
        }

        public static <T extends Collection<TagData>> T mapTagsToData(Collection<TagDto> dtos,
                                                                      Collector<TagData, ?, T> collector) {
            return dtos.stream()
                    .map(Utils::mapTagToData)
                    .collect(collector);
        }
    }
}