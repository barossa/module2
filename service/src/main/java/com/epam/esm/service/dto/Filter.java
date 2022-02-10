package com.epam.esm.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
public class Filter {
    private List<String> tags;
    private List<String> names;
    private List<String> descriptions;

    public Filter(){
        tags = new ArrayList<>();
        names = new ArrayList<>();
        descriptions = new ArrayList<>();
    }
}
