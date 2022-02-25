package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.PrePersist;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto extends RepresentationModel<OrderDto> {
    @JsonView(View.Base.class)
    private int id;

    @JsonView(View.Base.class)
    private LocalDateTime orderDate;

    @JsonView(View.Base.class)
    private BigDecimal cost;

    @JsonView(View.Full.class)
    private UserDto user;

    @JsonView(View.Full.class)
    private CertificateDto certificate;

    @PrePersist
    private void prePersist(){
        orderDate = LocalDateTime.now();
    }
}
