package com.rushr.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "RH_Notification")
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "USER_Id")
    private Long userId;

    @Column(name = "IS_VIEWED")
    private boolean isViewed;



}
