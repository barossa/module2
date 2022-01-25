package com.epam.esm.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Data
public class Certificate {
    private int id;
    private String name;
    private String description;
    private BigDecimal price;
    private Long duration;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private Set<Tag> tags = new HashSet<>();

    public Certificate(String name, String description, BigDecimal price, long duration) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
    }
}
