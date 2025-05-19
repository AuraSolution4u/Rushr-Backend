package com.rushr.Dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserDetailsRequestDTO {

    private Long signUpAs;

    private String profieLink;

    private String profilePicDir;

    private String profilePicName;

    private String firstName;

    private String lastName;

    private String emailId;

    private LocalDate dateOfBirth;

    private String password;

    private Long universityName;

    private String otherUniversityName;

    private String mobileNumber;

    private String bio;

    private List<String> tags;

    private String graduationYear;

    private Long major;

    private String notificationPreference;

    private String privacySettings;

}
