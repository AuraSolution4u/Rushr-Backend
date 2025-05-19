//package com.rushr.Entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//import java.util.Set;
//
//
//@Entity
//@Table(name = "RH_CHAPTER_TO_MEMBER_MAPPING")
//@Data
//public class ChapterTToMemberMapping {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @JoinTable(name = "MEMBER_ID",joinColumns = @JoinColumn(name = ""))
//    private Set<Usermaster> members;
//
//    @Column(name = "CHAPTER_Id")
//    private Long chapterId;
//
//
//}
