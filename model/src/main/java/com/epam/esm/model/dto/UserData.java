package com.epam.esm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserData {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id")
    private int id;

    @Column(name = "username",
            unique = true,
            nullable = false)
    private String username;
}
