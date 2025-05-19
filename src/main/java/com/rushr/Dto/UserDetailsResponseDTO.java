package com.rushr.Dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDetailsResponseDTO {

    private String signUpAs;

    private Long userId;

    private String profieLink;

    private String firstName;

    private String lastName;

    private String emailId;

    private Long universityId;

    private String universityName;

    private String otherUniversityName;

    private String mobileNumber;

    private String bio;

    private LocalDate dateOfBirth;

    private List<String> tags;

    private String graduationYear;

    private Long majorId;

    private String majorName;

    private String notificationPreference;

    private String privacySettings;

    private boolean isAdmin;

    private ChapterListDTO chapterDetails;

    private Integer approvedStatus;
}
