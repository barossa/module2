package com.epam.esm.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Set;

@Data
@NoArgsConstructor
public class Tag {
    private int id;
    private String name;
    private Set<Certificate> certificates = Collections.emptySet();

    public Tag(String name){
        this.name = name;
    }
}
