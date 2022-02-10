package com.epam.esm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageData {
    private int offset;
    private int limit;

    public PageData() {
        offset = 0;
        limit = 20;
    }
}
