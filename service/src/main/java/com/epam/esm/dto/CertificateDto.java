package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificateDto extends RepresentationModel<CertificateDto> {
    @JsonView(View.Base.class)
    private int id;

    @JsonView(View.Base.class)
    private String name;

    @JsonView(View.Base.class)
    private String description;

    @JsonView(View.Base.class)
    private BigDecimal price;

    @JsonView(View.Base.class)
    private Long duration;

    @JsonView(View.Base.class)
    private LocalDateTime createDate;

    @JsonView(View.Base.class)
    private LocalDateTime lastUpdateDate;

    @JsonView(View.Base.class)
    private Set<TagDto> tags;
}
