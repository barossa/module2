package com.epam.esm.dto;

import com.epam.esm.entity.*;
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

    public static CertificateFilter mapFilter(CertificateFilterDto filter) {
        CertificateFilter certificateFilter = new CertificateFilter();
        BeanUtils.copyProperties(filter, certificateFilter);
        return certificateFilter;
    }

    public static CertificateDto mapCertificateFromData(Certificate data) {
        CertificateDto dto = Utils.mapCertificateFromData(data);
        Set<Tag> tagsData = data.getTags();
        Set<TagDto> tagsDto = new HashSet<>();
        if (tagsData != null) {
            tagsDto = tagsData.stream()
                    .map(Utils::mapTagFromData)
                    .collect(Collectors.toSet());
        }
        dto.setTags(tagsDto);
        return dto;
    }

    public static Certificate mapCertificateToData(CertificateDto dto) {
        Certificate data = Utils.mapCertificateToData(dto);
        Set<TagDto> tagsDto = dto.getTags();
        Set<Tag> tagsData = null;
        if (tagsDto != null) {
            tagsData = tagsDto.stream()
                    .map(Utils::mapTagToData)
                    .collect(Collectors.toSet());
        }
        data.setTags(tagsData);
        return data;
    }

    public static TagDto mapTagFromData(Tag data) {
        TagDto dto = Utils.mapTagFromData(data);
        Set<Certificate> certificatesData = data.getCertificates();
        Set<CertificateDto> certificatesDto = new HashSet<>();
        if (certificatesData != null) {
            certificatesDto = certificatesData.stream()
                    .map(Utils::mapCertificateFromData)
                    .collect(Collectors.toSet());
        }
        dto.setCertificates(certificatesDto);
        return dto;
    }

    public static Tag mapTagToData(TagDto dto) {
        Tag data = Utils.mapTagToData(dto);
        Set<CertificateDto> certificatesDto = dto.getCertificates();
        Set<Certificate> certificatesData = new HashSet<>();
        if (certificatesDto != null) {
            certificatesData = certificatesDto.stream()
                    .map(Utils::mapCertificateToData)
                    .collect(Collectors.toSet());
        }
        data.setCertificates(certificatesData);
        return data;
    }

    public static UserDto mapUserFromData(User data) {
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(data, dto);
        return dto;
    }

    public static User mapUserToData(UserDto dto) {
        User data = new User();
        BeanUtils.copyProperties(dto, data);
        return data;
    }

    public static OrderDto mapOrderFromData(Order data) {
        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(data, orderDto);
        UserDto userDto = DtoMapper.mapUserFromData(data.getUser());
        CertificateDto certificateDto = DtoMapper.mapCertificateFromData(data.getCertificate());
        orderDto.setUser(userDto);
        orderDto.setCertificate(certificateDto);
        return orderDto;
    }

    public static Page mapPageToData(PageDto dto) {
        return new Page(dto.getPage() * dto.getLimit(), dto.getLimit());
    }

    public static <T extends Collection<UserDto>> T mapUsersFromData(Collection<User> data, Collector<UserDto, ?, T> collector) {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data.stream()
                .map(DtoMapper::mapUserFromData)
                .collect(collector);
    }


    public static <T extends Collection<OrderDto>> T mapOrdersFromData(Collection<Order> data, Collector<OrderDto, ?, T> collector) {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data.stream()
                .map(DtoMapper::mapOrderFromData)
                .collect(collector);
    }


    public static <T extends Collection<CertificateDto>> T mapCertificatesFromData(Collection<Certificate> data, Collector<CertificateDto, ?, T> collector) {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data.stream()
                .map(DtoMapper::mapCertificateFromData)
                .collect(collector);
    }

    public static <T extends Collection<TagDto>> T mapTagsFromData(Collection<Tag> data, Collector<TagDto, ?, T> collector) {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data.stream()
                .map(DtoMapper::mapTagFromData)
                .collect(collector);
    }

    public static <T extends Collection<Tag>> T mapTagsToData(Collection<TagDto> dtos,
                                                              Collector<Tag, ?, T> collector) {
        if (dtos == null) {
            dtos = new ArrayList<>();
        }
        return dtos.stream()
                .map(DtoMapper::mapTagToData)
                .collect(collector);
    }

    private static class Utils {

        public static CertificateDto mapCertificateFromData(Certificate data) {
            CertificateDto dto = new CertificateDto();
            BeanUtils.copyProperties(data, dto);
            return dto;
        }

        public static Certificate mapCertificateToData(CertificateDto dto) {
            Certificate data = new Certificate();
            BeanUtils.copyProperties(dto, data);
            return data;
        }

        public static TagDto mapTagFromData(Tag data) {
            TagDto dto = new TagDto();
            BeanUtils.copyProperties(data, dto);
            return dto;
        }

        public static Tag mapTagToData(TagDto dto) {
            Tag data = new Tag();
            BeanUtils.copyProperties(dto, data);
            return data;
        }

        public static <T extends Collection<CertificateDto>> T mapCertificatesFromData(Collection<Certificate> data,
                                                                                       Collector<CertificateDto, ?, T> collector) {
            return data.stream()
                    .map(Utils::mapCertificateFromData)
                    .collect(collector);
        }

        public static <T extends Collection<Certificate>> T mapCertificatesToData(Collection<CertificateDto> dtos,
                                                                                  Collector<Certificate, ?, T> collector) {
            return dtos.stream()
                    .map(Utils::mapCertificateToData)
                    .collect(collector);
        }

        public static <T extends Collection<TagDto>> T mapTagsFromData(Collection<Tag> data,
                                                                       Collector<TagDto, ?, T> collector) {
            return data.stream()
                    .map(Utils::mapTagFromData)
                    .collect(collector);
        }

        public static <T extends Collection<Tag>> T mapTagsToData(Collection<TagDto> dtos,
                                                                  Collector<Tag, ?, T> collector) {
            return dtos.stream()
                    .map(Utils::mapTagToData)
                    .collect(collector);
        }
    }
}