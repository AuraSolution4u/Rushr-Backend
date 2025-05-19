package com.rushr.Entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "RH_SIGN_UP_AS")
@Data
public class SignUpType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "SIGN_UP_TYPE")
    private String signUpType;

}
