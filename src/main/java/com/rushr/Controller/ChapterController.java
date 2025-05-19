package com.rushr.Controller;

import com.rushr.Dto.ChapterCreationDTO;
import com.rushr.Dto.ResponseDTO;
import com.rushr.Service.ChapterService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/restAPI/chapter")
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    @PostMapping(value = "/createChapter")
    public ResponseDTO createChapter(HttpServletRequest request, @RequestBody ChapterCreationDTO chapterCreationDTO)
    {
        return chapterService.createChapter(request,chapterCreationDTO);
    }

    @GetMapping(value = "/getChapterList")
    public ResponseDTO chaptersList(HttpServletRequest request)
    {
        return chapterService.chapterList(request);
    }

//    @PostMapping(value = "/addMembers")
//    public ResponseDTO addUsers(HttpServletRequest request,@RequestBody String jsonData)
//    {
//        return chapterService.addMembersToChapter(request,jsonData);
//    }

    @PostMapping(value = "/uploadFileToRepo")
    public ResponseDTO uploadFileToChapterRepo(HttpServletRequest request, @RequestParam("file")MultipartFile file,@RequestParam("userId") Long userId,@RequestParam("chapterId") Long chapterId)
    {
        return chapterService.uploadFileToRepo(request, file, userId, chapterId);
    }

    @PostMapping(value = "/getChapterRepoFilesByUserId")
    public ResponseDTO getChapterRepoFilesByUserId(HttpServletRequest request,@RequestBody String jsonData)
    {
        return chapterService.getChapterRepoFilesByUserId(request,jsonData);
    }

    @PostMapping(value = "/getChapterRepoFilesByChapterId")
    public ResponseDTO getChapterRepoFilesByChapterId(HttpServletRequest request,@RequestBody  String jsonData)
    {
        return chapterService.getChapterRepoFilesByChapterId(request,jsonData);
    }

    @PostMapping(value = "/assignAdmin")
    public ResponseDTO assignAdmin(HttpServletRequest request,@RequestBody String jsonData)
    {
        return chapterService.assignAdmin(request,jsonData);
    }

    @PostMapping(value = "/unAssignAdmin")
    public ResponseDTO unAssignAdmin(HttpServletRequest request,@RequestBody String jsonData) {
        return chapterService.unAssignAdmin(request, jsonData);
    }

    @PostMapping(value = "/getAllUsersByChapterId")
    public ResponseDTO getAllUsersByChapterId(HttpServletRequest request,@RequestBody String jsonData) {
        return chapterService.getAllUsersByChapterId(request, jsonData);
    }

    @PostMapping(value = "/approveOrRejectChapterMemberRequest")
    public ResponseDTO approveOrRejectChapterMemberRequest(HttpServletRequest request,@RequestBody String jsonData) {
        return chapterService.approveOrRejectChapterMemberRequest(request, jsonData);
    }
}
