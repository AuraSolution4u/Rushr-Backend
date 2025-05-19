package com.rushr.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "RH_OTP_GENERATION")
@Data
public class OTPGeneration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "OTP")
    private String otp;

    @Column(name = "GENERATED_TIME")
    private LocalDateTime generatedTime;

    @Column(name = "EXPIRY_TIME")
    private LocalDateTime expiryTime;

    @Column(name = "STATUS")
    private boolean status;

//    @Column(name = "USER_ID")
//    private Long userId;

    @Column(name = "EMAIL_ID")
    private String emailId;


}
