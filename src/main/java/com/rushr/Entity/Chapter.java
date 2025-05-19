package com.rushr.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.apache.catalina.User;

import java.util.List;
import java.util.*;

@Entity
@Table(name = "RH_CHAPTER")
@Data
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chapterId;

    @ManyToOne
    @JoinColumn(name = "CREATED_BY",columnDefinition = "bigint")
    private Usermaster createdBy;

    @Column(name = "DIRECTORY")
    private String directory;

    @Column(name = "COVER_PHOTO_NAME")
    private String coverPhotoName;

    @Column(name = "PROFILE_PHOTO_NAME")
    private String profilePhotoName;

    @Column(name = "COVER_PHOTO_LINK")
    private String coverPhotoLink;

    @Column(name = "CHAPTER_PROFILE_PHOTO_LINK")
    private String chapterProfilePhotoLink;

    @Column(name = "GREEK_CHAPTER_NAMES")
    private String greekChapterNames;

    @Column(name = "CHAPTER_NAME")
    private String chapterName;

    @Column(name = "CHAPTER_DESCRIPTION")
    private String chapterDescription;

    @Column(name = "CHAPTER_HISTORY")
    private String chapterHistory;

    @Column(name = "CHAPTER_MISSION")
    private String chapterMission;

    @Column(name = "LOCATION")
    private String location;

    @Column(name = "ESTABLISHED_YEAR")
    private String establishedYear;

    @Column(name = "TAGS")
    private List<String> tags;

    @Column(name = "MEMBERS")
    private List<Long> members;

    @OneToMany(mappedBy = "chapterId",cascade = CascadeType.ALL)
    private List<Role> roleList;

    @Column(name = "CHAPTER_RULES_AND_GUIDELINES")
    private String chapterRulesAndGuidelines;

    @ManyToOne
    @JoinColumn(name = "ADMIN_Id",columnDefinition = "bigint")
    private Usermaster adminId;

//    @ManyToMany(mappedBy = "chapters")
//    private Set<Usermaster> chapterMembers=new HashSet<>();

//    @ManyToOne
//    @JoinColumn(name = "admin_id",referencedColumnName = "userId",nullable = false)
//    private Usermaster admin;


}
