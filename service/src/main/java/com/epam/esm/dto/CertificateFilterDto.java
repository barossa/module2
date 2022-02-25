package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class CertificateFilterDto {
    private List<String> tags;
    private List<String> names;
    private List<String> descriptions;

    public CertificateFilterDto() {
        tags = new ArrayList<>();
        names = new ArrayList<>();
        descriptions = new ArrayList<>();
    }
}
