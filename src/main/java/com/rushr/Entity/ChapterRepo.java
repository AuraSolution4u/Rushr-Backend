package com.rushr.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "RH_CHAPTER_REPO")
@Data
public class ChapterRepo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "CHAPTER_ID")
    private Long chapterId;

    @Column(name = "DIRECTORY")
    private String directory;

    @Column(name = "FILE_NAME")
    private String fileName;


}
