package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDto extends RepresentationModel<TagDto> {
    @JsonView(View.Base.class)
    private int id;

    @JsonView(View.Base.class)
    private String name;

    @JsonView(View.Full.class)
    private Set<CertificateDto> certificates = new HashSet<>();

    public TagDto(String name) {
        this.name = name;
    }
}
