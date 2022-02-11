package com.epam.esm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;
import static org.hibernate.annotations.FetchMode.JOIN;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class OrderData {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "order_id")
    private int id;

    @Column(name = "order_date",
            nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "cost",
            nullable = false)
    private BigDecimal cost;

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(JOIN)
    @JoinColumn(name = "user_id")
    private UserData user;

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(JOIN)
    @JoinColumn(name = "certificate_id")
    private CertificateData certificate;
}
