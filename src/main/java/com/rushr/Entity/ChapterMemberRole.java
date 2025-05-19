//package com.rushr.Entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//@Entity
//@Table(name = "RH_CHAPTER_MEMBER_ROLE")
//@Data
//public class ChapterMemberRole {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "CHAPTER_ID",columnDefinition = "bigint")
//    private Chapter chapterId;
//
//    @ManyToOne
//    @JoinColumn(name = "ROLE_ID",columnDefinition = "bigint")
//    private Role roleId;
//
//    @ManyToOne
//    @JoinColumn(name = "USER_ID",columnDefinition = "bigint")
//    private Usermaster userId;
//}
