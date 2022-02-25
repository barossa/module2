package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageDto {
    private int page;
    private int limit;

    public PageDto(){
        page = 0;
        limit = 20;
    }
}
