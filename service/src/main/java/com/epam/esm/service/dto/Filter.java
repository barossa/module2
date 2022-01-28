package com.epam.esm.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Filter {
    private List<String> tags;
    private List<String> names;
    private List<String> descriptions;
    private boolean strong;
}
