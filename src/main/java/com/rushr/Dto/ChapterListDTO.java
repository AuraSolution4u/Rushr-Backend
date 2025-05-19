package com.rushr.Dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ChapterListDTO {

    private Long chapterId;

    private String createdBy;

    private String coverPhotoLink;

    private String chapterProfilePhotoLink;

    private String greekChapterNames;

    private String chapterName;

    private String chapterDescription;

    private String chapterHistory;

    private String chapterMission;

    private String location;

    private String establishedYear;

    private List<String> tags;

    private List<UserDetailsResponseDTO> chapterMembers;

    private String chapterRulesAndGuidelines;

    private AdminDetailDTO adminDetails;

    private List<Long> members;


}
