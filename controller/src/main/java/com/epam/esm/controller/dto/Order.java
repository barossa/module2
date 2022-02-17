package com.epam.esm.controller.dto;

import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Order {
    private int id;
    private LocalDateTime orderDate;
    private BigDecimal cost;
}
