package com.epam.esm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Page {
    private int offset;
    private int limit;

    public Page() {
        offset = 0;
        limit = 20;
    }
}
