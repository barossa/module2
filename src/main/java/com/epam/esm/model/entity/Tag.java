package com.epam.esm.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class Tag {
    private int id;
    private String name;
    private Set<Certificate> certificates = new HashSet<>();

    public Tag(String name) {
        this.name = name;
    }
}
