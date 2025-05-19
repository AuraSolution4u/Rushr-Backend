package com.rushr.Dto;

import lombok.Data;

import java.util.List;

@Data
public class ChapterCreationDTO {

    private Long userId;

//    private String coverPhotoLink;
//
//    private String chapterProfilePhotoLink;

    private String directory;

    private String profilePicName;

    private String coverPicName;

    private String greekChapterNames;

    private String chapterName;

    private String chapterDescription;

    private String chapterHistory;

    private String chapterMission;

    private String location;

    private String establishedYear;

    private List<String> tags;

    private List<String> roleNames;

    private List<ChapterMemberRoleRequestDTO> assignRoles;

    private String chapterRulesAndGuidelines;

}
