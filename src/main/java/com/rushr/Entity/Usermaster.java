package com.rushr.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;


@Entity
@Table(name = "RH_USERMASTER")
@Data
public class Usermaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "SIGN_UP_TYPE_ID",columnDefinition = "bigint")
    private SignUpType signUpTypeId;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "EMAIL_ID")
    private String emailId;

    @ManyToOne
    @JoinColumn(name = "UNIVERSITY_ID",columnDefinition = "bigint")
    private UniversityMaster universityId;

    @Column(name = "OTHER_UNIVERSITY_NAME")
    private String otherUniversityName;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "BIO")
    private String bio;

    @Column(name = "TAGS")
    private List<String> tags;

    @Column(name = "GRADUATION_YEAR")
    private String graduationYear;

    @ManyToOne
    @JoinColumn(name = "MAJOR_ID",columnDefinition = "bigint")
    private MajorsListMaster majorId;

    @Column(name = "NOTIFICATION_PREFERENCE")
    private String notificationPreference;

    @Column(name = "PRIVACY_SETTINGS")
    private String privacySettings;

    @Column(name = "STATUS")
    private boolean status;

    @Column(name = "MOBILE_NUMBER")
    private String mobileNumber;

    @Column(name = "PROFILE_PIC_DIR")
    private String ProfilePicDir;

    @Column(name = "PROFILE_PIC_NAME")
    private String profilePicName;

    @Column(name = "DATE_OF_BIRTH")
    private LocalDate dateOfBirth;

    @Column(name = "REGISTERED_ON")
    private LocalDateTime registeredOn;

    @ManyToOne
    @JoinColumn(name = "CHAPTER_ID",columnDefinition = "bigint",nullable = true)
    private Chapter chapterId;

    @Column(name = "CHAPTER_APPROVED_STATUS")
    private Integer approvedStatus;

    @ManyToOne
    @JoinColumn(name = "CHAPTER_APPROVED_BY",columnDefinition = "bigint")
    private Usermaster approvedBy;

    @Column(name = "CHAPTER_APPROVAL_REQUEST_ON")
    private LocalDateTime requestedOn;

    @Column(name = "CHAPTER_APPROVE_RESPONDED_ON")
    private LocalDateTime respondedOn;

    @ManyToOne
    @JoinColumn(name = "CHAPTER_REQUESTED_BY",columnDefinition = "bigint")
    private Usermaster requestedBy;
//    @ManyToMany
//    @JoinTable(name = "RH_CHAPTER_MEMBERS,",joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "chapter_id"))
//    private Set<Chapter> chapters=new HashSet<>();
}
