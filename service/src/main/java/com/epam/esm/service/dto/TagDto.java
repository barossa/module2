package com.epam.esm.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDto {
    private int id;
    private String name;
    private Set<CertificateDto> certificates = new HashSet<>();

    public TagDto(String name) {
        this.name = name;
    }
}
