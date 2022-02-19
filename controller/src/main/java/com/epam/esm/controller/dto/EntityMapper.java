package com.epam.esm.controller.dto;

import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.UserDto;
import org.springframework.beans.BeanUtils;

import java.util.Collection;
import java.util.stream.Collector;

public final class EntityMapper {

    private EntityMapper() {
    }

    public static Tag mapTagFromDto(TagDto dto) {
        return new Tag(dto.getId(), dto.getName());
    }

    public static Order mapOrderFromDto(OrderDto dto) {
        return new Order(dto.getId(),
                dto.getOrderDate(),
                dto.getCost());
    }

    public static Certificate mapCertificateFromDto(CertificateDto dto) {
        Certificate certificate = new Certificate();
        BeanUtils.copyProperties(dto, certificate);
        return certificate;
    }

    public static User mapUserFromDto(UserDto dto) {
        User user = new User();
        BeanUtils.copyProperties(dto, user);
        return user;
    }

    public static <T extends Collection<Tag>> T mapTagsFromDto(Collection<TagDto> dtos,
                                                               Collector<Tag, ?, T> collector) {
        T tags = null;
        if (dtos != null) {
            tags = dtos.stream()
                    .map(EntityMapper::mapTagFromDto)
                    .collect(collector);
        }
        return tags;
    }

    public static <T extends Collection<Order>> T mapOrdersFromDto(Collection<OrderDto> dtos,
                                                                   Collector<Order, ?, T> collector) {
        T orders = null;
        if (dtos != null) {
            orders = dtos.stream()
                    .map(EntityMapper::mapOrderFromDto)
                    .collect(collector);
        }
        return orders;
    }

    public static <T extends Collection<Certificate>> T mapCertificatesFromDto(Collection<CertificateDto> dtos,
                                                                               Collector<Certificate, ?, T> collector) {
        T certificates = null;
        if (dtos != null) {
            certificates = dtos.stream()
                    .map(EntityMapper::mapCertificateFromDto)
                    .collect(collector);
        }
        return certificates;
    }

    public static <T extends Collection<User>> T mapUsersFromDto(Collection<UserDto> dtos,
                                                                 Collector<User, ?, T> collector) {
        T users = null;
        if (dtos != null) {
            users = dtos.stream()
                    .map(EntityMapper::mapUserFromDto)
                    .collect(collector);
        }
        return users;
    }

}
