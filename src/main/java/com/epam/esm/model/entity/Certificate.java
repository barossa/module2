package com.epam.esm.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

@Data
@NoArgsConstructor
public class Certificate {
    private int id;
    private String name;
    private String description;
    private BigDecimal price;
    private Long duration;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private Set<Tag> tags = Collections.emptySet();

    public Certificate(String name, String description, BigDecimal price, long duration) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
    }
}
