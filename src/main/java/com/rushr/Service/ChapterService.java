package com.rushr.Service;

import com.rushr.Dto.*;
import com.rushr.Entity.Chapter;
import com.rushr.Entity.ChapterRepo;
import com.rushr.Entity.Usermaster;
import com.rushr.Repository.*;
import com.rushr.Util.Constants;
import com.rushr.Util.EmptyResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChapterService {

    private static final Logger log= LogManager.getLogger(ChapterService.class);

    @Autowired
    private UsermasterRepository usermasterRepository;

    @Autowired
    private ChapterCreationRepository chapterCreationRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private ChapterRepoRepository chapterRepoRepository;

    private final EmptyResponseUtil emptyResponseUtil=new EmptyResponseUtil();

        public ResponseDTO createChapter(HttpServletRequest request, ChapterCreationDTO chapterCreationDTO)
        {
            ResponseDTO responseDTO=new ResponseDTO();
            try {
                Usermaster usermaster=usermasterRepository.findByUserId(chapterCreationDTO.getUserId());
                if(usermaster==null)
                {
                    log.warn(Constants.Messages.noUserFound);
                    return emptyResponseUtil.emptyUserResponse();
                }

                if(chapterCreationDTO.getDirectory()==null || chapterCreationDTO.getDirectory().isEmpty())
                {
                    return emptyResponseUtil.emptyResponse("Empty Directory Name");
                }
    
                if(chapterCreationDTO.getCoverPicName()==null || chapterCreationDTO.getCoverPicName().isEmpty())
                {
                    return emptyResponseUtil.emptyResponse("Empty Cover Photo Name");
                }
    
                if(chapterCreationDTO.getProfilePicName()==null || chapterCreationDTO.getProfilePicName().isEmpty())
                {
                    return emptyResponseUtil.emptyResponse("Empty Profile Photo Name");
                }
    
                if(chapterCreationDTO.getGreekChapterNames()==null)
                {
                    return emptyResponseUtil.emptyResponse("Empty Greek Chapter Name");
                }
    
                if(chapterCreationDTO.getChapterName()==null || chapterCreationDTO.getChapterName().isEmpty())
                {
                    return emptyResponseUtil.emptyResponse("Empty Chapter Name");
                }
    
                if(chapterCreationDTO.getChapterDescription()==null || chapterCreationDTO.getChapterDescription().isEmpty())
                {
                    return emptyResponseUtil.emptyResponse("Empty Chapter Description");
                }
    
                if(chapterCreationDTO.getChapterHistory()==null || chapterCreationDTO.getChapterHistory().isEmpty())
                {
                    return emptyResponseUtil.emptyResponse("Empty Chapter History");
                }
    
                if(chapterCreationDTO.getChapterMission()==null || chapterCreationDTO.getChapterMission().isEmpty())
                {
                    return emptyResponseUtil.emptyResponse("Empty Chapter Mission");
                }
    
                if(chapterCreationDTO.getLocation()==null || chapterCreationDTO.getLocation().isEmpty())
                {
                    return emptyResponseUtil.emptyResponse("Empty Location");
                }
    
                if(chapterCreationDTO.getEstablishedYear()==null || chapterCreationDTO.getEstablishedYear().isEmpty())
                {
                    return emptyResponseUtil.emptyResponse("Empty Established Year");
                }
    
                if(chapterCreationDTO.getTags()==null)
                {
                    return emptyResponseUtil.emptyResponse("Empty Tag List");
                }

                //Saving Chapter Details
                Chapter chapter=new Chapter();
                chapter.setCreatedBy(usermaster);
                chapter.setDirectory(chapterCreationDTO.getDirectory());
                chapter.setCoverPhotoName(chapterCreationDTO.getCoverPicName());
                chapter.setProfilePhotoName(chapterCreationDTO.getProfilePicName());
                chapter.setGreekChapterNames(chapterCreationDTO.getGreekChapterNames());
                chapter.setChapterName(chapterCreationDTO.getChapterName());
                chapter.setChapterDescription(chapterCreationDTO.getChapterDescription());
                chapter.setChapterHistory(chapterCreationDTO.getChapterHistory());
                chapter.setLocation(chapterCreationDTO.getLocation());
                chapter.setEstablishedYear(chapterCreationDTO.getEstablishedYear());
                chapter.setTags(chapterCreationDTO.getTags());
                chapter.setChapterMission(chapterCreationDTO.getChapterMission());
                chapter.setChapterRulesAndGuidelines(chapterCreationDTO.getChapterRulesAndGuidelines());
//                //Saving Role Details
//                List<Role> roleList=new ArrayList<>();
//                roleList.addAll((chapterCreationDTO.getRoleNames()).stream().map((vl)->{
//                    Role rl=new Role();
//                    rl.setRoleName(vl);
//                    rl.setChapterId(chapter);
//                    return rl;
//                }).collect(Collectors.toList()));
//
//                chapter.setRoleList(roleList);

                chapterCreationRepository.save(chapter);


//                //Saving Member Details
//
//                for(ChapterMemberRoleRequestDTO vls:chapterCreationDTO.getAssignRoles())
//                {
//                    ChapterMemberRole chapterMemberRole=new ChapterMemberRole();
//                    // To get Role details
//                    Role role=roleRepository.findByRoleNameAndChapterId(vls.getRoleName(),chapter);
//                    chapterMemberRole.setChapterId(chapter);
//                    chapterMemberRole.setRoleId(role);
//
//                    //To get User Details
//                    Usermaster userDetails=usermasterRepository.findByUserId(vls.getUserId());
//                    chapterMemberRole.setUserId(userDetails);
//
//                    chapterMemberRoleRepository.save(chapterMemberRole);
//                }

                log.info(Constants.Messages.success);
                responseDTO.setMessage(Constants.Messages.success);
                responseDTO.setStatusCode(Constants.StatusCodes.success);
            }
            catch (Exception e)
            {
                log.error(Constants.Messages.error);
    
                e.printStackTrace();
                responseDTO.setMessage(Constants.Messages.error);
                responseDTO.setStatusCode(Constants.StatusCodes.error);
            }
            return responseDTO;
        }

    public ResponseDTO chapterList(HttpServletRequest request)
    {
        ResponseDTO responseDTO=new ResponseDTO();
        try {

            List<ChapterListDTO> chaptersList=new ArrayList<>();

            List<Chapter> list=chapterCreationRepository.findAll();

            if(list==null)
            {
                log.warn("Empty Chapters List");
                responseDTO.setMessage("Empty Chapters List");
                responseDTO.setStatusCode(Constants.StatusCodes.noContent);
                return responseDTO;
            }

            for ( Chapter chapter : list){
                List <Long> memberIds = new ArrayList<>();
                List<Usermaster> users = usermasterRepository.findByChapterId(chapter);
                for (Usermaster user : users){
                    if (user.getApprovedStatus().equals(1)){
                        memberIds.add(user.getUserId());
                    }
                }
                chapter.setMembers(memberIds);
                chaptersList.add(getChapterDetails(chapter));
            }

//            chaptersList.addAll(list.stream().map(this::getChapterDetails).toList());

            log.info(Constants.Messages.success);

            responseDTO.setMessage(Constants.Messages.success);
            responseDTO.setStatusCode(Constants.StatusCodes.success);
            responseDTO.setResponseObject(chaptersList);
        }
        catch (Exception e)
        {
            log.error(Constants.Messages.error);

            e.printStackTrace();
            responseDTO.setMessage(Constants.Messages.error);
            responseDTO.setStatusCode(Constants.StatusCodes.error);
        }

        return responseDTO;
    }

    public ChapterListDTO getChapterDetails(Chapter chapterDetails)
    {
        ChapterListDTO dto=new ChapterListDTO();
        dto.setChapterId(chapterDetails.getChapterId());
        dto.setCreatedBy(chapterDetails.getCreatedBy().getFirstName()+" "+chapterDetails.getCreatedBy().getLastName());


        //To Get Cover Photo URL from S3 Bucket
        if(chapterDetails.getCoverPhotoName()!=null)
        {
            String coverPhotoLink=s3Service.accessFile(chapterDetails.getDirectory(), chapterDetails.getCoverPhotoName());
            dto.setCoverPhotoLink(coverPhotoLink);
        }

        //To get Profile Photo URL from S3 Bucket
        if(chapterDetails.getProfilePhotoName()!=null)
        {
            String profilePhotoLink=s3Service.accessFile(chapterDetails.getDirectory(),chapterDetails.getProfilePhotoName());
            dto.setChapterProfilePhotoLink(profilePhotoLink);
        }


        dto.setGreekChapterNames(chapterDetails.getGreekChapterNames());
        dto.setChapterName(chapterDetails.getChapterName());
        dto.setChapterDescription(chapterDetails.getChapterDescription());
        dto.setChapterHistory(chapterDetails.getChapterHistory());
        dto.setChapterMission(chapterDetails.getChapterMission());
        dto.setLocation(chapterDetails.getLocation());
        dto.setEstablishedYear(chapterDetails.getEstablishedYear());
        dto.setTags(chapterDetails.getTags());
        dto.setChapterMission(chapterDetails.getChapterMission());
        dto.setChapterRulesAndGuidelines(chapterDetails.getChapterRulesAndGuidelines());


        log.info("Chapter Admin Details: {}",chapterDetails.getAdminId());

        if(chapterDetails.getAdminId()!=null )
        {
            //To get Chapter Details
            AdminDetailDTO adminDetailDTO=new AdminDetailDTO();
            adminDetailDTO.setAdminName(chapterDetails.getAdminId().getFirstName()+" "+chapterDetails.getAdminId().getLastName());
            adminDetailDTO.setSignUpType(chapterDetails.getAdminId().getSignUpTypeId().getSignUpType());
            adminDetailDTO.setUserId(chapterDetails.getAdminId().getUserId());

            if(chapterDetails.getAdminId().getUniversityId().getUniversityName().equalsIgnoreCase("others"))
            {
                adminDetailDTO.setUniversityName(chapterDetails.getAdminId().getOtherUniversityName());
            }
            else
            {
                adminDetailDTO.setUniversityName(chapterDetails.getAdminId().getUniversityId().getUniversityName());
            }

            dto.setAdminDetails(adminDetailDTO);
        }


        List<UserDetailsResponseDTO> memberDetailsList=new ArrayList<>();
        dto.setMembers(chapterDetails.getMembers());

//                memberDetailsList.addAll((chapterToMemberMappingRepository.findByChapterId(val.getChapterId())).stream().map((member)->{
//
//
//                    //To Get Member Details
//                    Usermaster memDetails=usermasterRepository.findByUserId(member.getMemberId());
//
//                    UserDetailsResponseDTO memDto=loginService.userDetails(memDetails);
//
//                    return memDto;
//                }).collect(Collectors.toList()));
//
//                dto.setChapterMembers(memberDetailsList);


//                //Getting Chapter Member Details
//                List<ChapterMemberRoleResponseDTO> memebersList=new ArrayList<>();
//
//                List<ChapterMemberRole> details=chapterMemberRoleRepository.findByChapterId(val);
//
//                memebersList.addAll((chapterMemberRoleRepository.findByChapterId(val)).stream().map((mem)->{
//                    ChapterMemberRoleResponseDTO memberDto=new ChapterMemberRoleResponseDTO();
//                    //Role Details
//                    Role roleDetails=roleRepository.findByRoleId(mem.getRoleId().getRoleId());
//                    memberDto.setRoleName(roleDetails.getRoleName());
//                    memberDto.setName(mem.getUserId().getFirstName()+" "+mem.getUserId().getLastName());
//                    return memberDto;
//                }).collect(Collectors.toList()));
//
//                dto.setChapterMembers(memebersList);

        return dto;

    }

//    public ResponseDTO addMembersToChapter(HttpServletRequest request, String jsonData)
//    {
//        ResponseDTO responseDTO=new ResponseDTO();
//        try {
//
//            if(jsonData==null)
//            {
//                log.warn(Constants.Messages.emptyRequestBody);
//                responseDTO.setMessage(Constants.Messages.emptyRequestBody);
//                responseDTO.setStatusCode(Constants.StatusCodes.noContent);
//                return responseDTO;
//            }
//
//            JSONObject jsonObject=new JSONObject(jsonData);
//            Long userId=jsonObject.optLong("userId");
//            Long chapterId=jsonObject.optLong("chapterId");
//            Long memberId=jsonObject.optLong("memberId");
//
//            //To Get Super Admin Details
//            Usermaster superAdminDetails=usermasterRepository.findByUserId(userId);
//
//            if(superAdminDetails==null)
//            {
//                log.warn(Constants.Messages.noUserFound);
//                return emptyResponseUtil.emptyUserResponse();
//            }
//
//            //Validating that User is Super Admin
//            if(superAdminDetails.getSignUpTypeId().getId()!=4)
//            {
//                log.warn(Constants.Messages.unAuthorized);
//                responseDTO.setMessage(Constants.Messages.unAuthorized);
//                responseDTO.setStatusCode(Constants.StatusCodes.unAuthorized);
//                return responseDTO;
//            }
//
//
//            //To Get Chapter Details
//            Chapter chapterDetails=chapterCreationRepository.findByChapterId(chapterId);
//
//            if(chapterDetails==null)
//            {
//                log.warn("Chapter Not Found");
//                responseDTO.setMessage("Chapter Not Found");
//                responseDTO.setStatusCode(Constants.StatusCodes.noContent);
//                return responseDTO;
//            }
//
//            //To Get Members Details
//            Usermaster memberDetails=usermasterRepository.findByUserId(memberId);
//
//            if(memberDetails==null)
//            {
//                log.warn(Constants.Messages.noUserFound);
//                responseDTO.setMessage(Constants.Messages.noUserFound);
//                responseDTO.setStatusCode(Constants.StatusCodes.notFound);
//                return responseDTO;
//            }
//
////            ChapterTToMemberMapping chapterTToMemberMapping =new ChapterTToMemberMapping();
////
////            chapterTToMemberMapping.setMemberId(memberId);
////            chapterTToMemberMapping.setChapterId(chapterId);
////
////            chapterToMemberMappingRepository.save(chapterTToMemberMapping);
//
//            log.info(Constants.Messages.success);
//            responseDTO.setMessage(Constants.Messages.success);
//            responseDTO.setStatusCode(Constants.StatusCodes.success);
//
//
//        }
//        catch (Exception e)
//        {
//            log.error(Constants.Messages.error,e);
//
//            e.printStackTrace();
//            responseDTO.setMessage(Constants.Messages.error);
//            responseDTO.setStatusCode(Constants.StatusCodes.error);
//        }
//
//        return responseDTO;
//    }


    public ResponseDTO uploadFileToRepo(HttpServletRequest request, MultipartFile file,Long userId,Long chapterId)
    {
        ResponseDTO responseDTO=new ResponseDTO();
        String directory="chapter/repo";

        try {

            if(file.isEmpty())
            {
                log.warn("Upload a Multipart File");
                responseDTO.setMessage("Upload a Multipart File");
                responseDTO.setStatusCode(Constants.StatusCodes.noContent);
                return responseDTO;
            }

            Usermaster userDetails=usermasterRepository.findByUserId(userId);
            if(userDetails==null)
            {
                log.warn(Constants.Messages.noUserFound);
                return emptyResponseUtil.emptyUserResponse();
            }

            Chapter chapterDetails=chapterCreationRepository.findByChapterId(chapterId);
            if(chapterDetails==null)
            {
                log.warn(Constants.Messages.chapterNotFound);
                responseDTO.setMessage(Constants.Messages.chapterNotFound);
                responseDTO.setStatusCode(Constants.StatusCodes.noContent);

            }

            //Call Upload Method -- To Upload a File to S3 Bucket
            Map<String,String> fileDetails=s3Service.getFileDetails(file,directory,userId);



            //Customized File Name
//            String fileName=System.currentTimeMillis()+"_"+userId+"_"+file.getOriginalFilename();

            ChapterRepo chapterRepo=new ChapterRepo();
            chapterRepo.setUserId(userId);
            chapterRepo.setChapterId(chapterId);
            chapterRepo.setDirectory(fileDetails.get("directory"));
            chapterRepo.setFileName(fileDetails.get("fileName"));
            chapterRepoRepository.save(chapterRepo);

            log.info(Constants.Messages.success);
            responseDTO.setMessage(Constants.Messages.success);
            responseDTO.setStatusCode(Constants.StatusCodes.success);

        }
        catch (Exception e)
        {
            log.error(Constants.Messages.error,e);

            e.printStackTrace();
            responseDTO.setMessage(Constants.Messages.error);
            responseDTO.setStatusCode(Constants.StatusCodes.error);
        }

        return responseDTO;
    }

    public ResponseDTO getChapterRepoFilesByUserId(HttpServletRequest request,@RequestBody String jsonData)
    {
        ResponseDTO responseDTO=new ResponseDTO();
        try {

            if(jsonData==null)
            {
                log.warn(Constants.Messages.emptyRequestBody);
                responseDTO.setMessage(Constants.Messages.emptyRequestBody);
                responseDTO.setStatusCode(Constants.StatusCodes.noContent);
                return responseDTO;
            }

            JSONObject jsonObject=new JSONObject(jsonData);
            Long userId=jsonObject.optLong("userId");

            Usermaster userDetails=usermasterRepository.findByUserId(userId);
            if(userDetails==null)
            {
                log.warn(Constants.Messages.noUserFound);
                return emptyResponseUtil.emptyUserResponse();
            }

            List<ChapterRepoFilesDTO> filesList=new ArrayList<>();

            filesList.addAll((chapterRepoRepository.findByUserId(userId)).stream().map((val)->{
                ChapterRepoFilesDTO dto=new ChapterRepoFilesDTO();
                dto.setUploadedByYou(true);

                String url=s3Service.accessFile(val.getDirectory(), val.getFileName());
                dto.setUrl(url);
                return dto;
            }).collect(Collectors.toList()));

            log.info(Constants.Messages.success);
            responseDTO.setMessage(Constants.Messages.success);
            responseDTO.setStatusCode(Constants.StatusCodes.success);
            responseDTO.setResponseObject(filesList);

        }
        catch (Exception e)
        {
            log.error(Constants.Messages.error,e);

            e.printStackTrace();
            responseDTO.setMessage(Constants.Messages.error);
            responseDTO.setStatusCode(Constants.StatusCodes.error);
        }
        return responseDTO;
    }

    public ResponseDTO getChapterRepoFilesByChapterId(HttpServletRequest request,@RequestBody String jsonData)
    {
        ResponseDTO responseDTO=new ResponseDTO();
        try {

            if(jsonData==null)
            {
                log.warn(Constants.Messages.emptyRequestBody);
                responseDTO.setMessage(Constants.Messages.emptyRequestBody);
                responseDTO.setStatusCode(Constants.StatusCodes.noContent);
                return responseDTO;
            }

            JSONObject jsonObject=new JSONObject(jsonData);
            Long userId=jsonObject.optLong("userId");
            Long chapterId=jsonObject.optLong("chapterId");



            Usermaster userDetails=usermasterRepository.findByUserId(userId);
            if(userDetails==null)
            {
                log.warn(Constants.Messages.noUserFound);
               return emptyResponseUtil.emptyUserResponse();
            }

            List<ChapterRepoFilesDTO> filesList=new ArrayList<>();

            filesList.addAll((chapterRepoRepository.findByChapterId(chapterId)).stream().map((val)->{
                ChapterRepoFilesDTO dto=new ChapterRepoFilesDTO();

                log.info("Uploaded By You: "+val.getUserId());

                if(val.getUserId().equals(userId))
                {
                    dto.setUploadedByYou(true);
                }
                else
                {
                    dto.setUploadedByYou(false);
                }


                String url=s3Service.accessFile(val.getDirectory(), val.getFileName());
                dto.setUrl(url);
                return dto;
            }).collect(Collectors.toList()));

            log.info(Constants.Messages.success);
            responseDTO.setMessage(Constants.Messages.success);
            responseDTO.setStatusCode(Constants.StatusCodes.success);
            responseDTO.setResponseObject(filesList);

        }
        catch (Exception e)
        {
            log.error(Constants.Messages.error,e);

            e.printStackTrace();
            responseDTO.setMessage(Constants.Messages.error);
            responseDTO.setStatusCode(Constants.StatusCodes.error);
        }
        return responseDTO;
    }

    public ResponseDTO assignAdmin(HttpServletRequest request,String jsonData)
    {
        ResponseDTO responseDTO=new ResponseDTO();
        try {
            if(jsonData==null)
            {
                log.warn(Constants.Messages.emptyRequestBody);
                responseDTO.setMessage(Constants.Messages.emptyRequestBody);
                responseDTO.setStatusCode(Constants.StatusCodes.noContent);
                return responseDTO;
            }

            JSONObject jsonObject=new JSONObject(jsonData);
            Long userId=jsonObject.optLong("userId");
            Long chapterId=jsonObject.optLong("chapterId");

            Usermaster userDetails=usermasterRepository.findByUserId(userId);
            if(userDetails==null)
            {
                log.warn(Constants.Messages.noUserFound);
                return emptyResponseUtil.emptyUserResponse();
            }

            //To Get Chapter Details
            Chapter chapter=chapterCreationRepository.findByChapterId(chapterId);
            chapter.setAdminId(userDetails);
            chapterCreationRepository.save(chapter);

            //To Assign Chapter to a user
            userDetails.setChapterId(chapter);
            userDetails.setApprovedStatus(1);
            usermasterRepository.save(userDetails);
            
            log.info(Constants.Messages.success);
            responseDTO.setMessage(Constants.Messages.success);
            responseDTO.setStatusCode(Constants.StatusCodes.success);
        }
        catch (Exception e)
        {
            log.error(Constants.Messages.error,e);

            e.printStackTrace();
            responseDTO.setMessage(Constants.Messages.error);
            responseDTO.setStatusCode(Constants.StatusCodes.error);
        }

        return responseDTO;
    }


    public ResponseDTO unAssignAdmin(HttpServletRequest request,String jsonData)
    {
        ResponseDTO responseDTO=new ResponseDTO();
        try {
            if(jsonData==null)
            {
                log.warn(Constants.Messages.emptyRequestBody);
                responseDTO.setMessage(Constants.Messages.emptyRequestBody);
                responseDTO.setStatusCode(Constants.StatusCodes.noContent);
                return responseDTO;
            }

            JSONObject jsonObject=new JSONObject(jsonData);
            Long superAdminId=jsonObject.optLong("superAdminId");
            Long chapterId= jsonObject.getLong("chapterId");
            Long userId=jsonObject.optLong("userId");

            //Super Admin Details
            Usermaster superAdminDetails=usermasterRepository.findByUserId(superAdminId);
            if(superAdminDetails.getSignUpTypeId().getId()!=4)
            {
                log.warn(Constants.Messages.unAuthorized);
                responseDTO.setMessage(Constants.Messages.unAuthorized);
                responseDTO.setStatusCode(Constants.StatusCodes.unAuthorized);
                return responseDTO;
            }

            //User Details
            Usermaster userDetails=usermasterRepository.findByUserId(userId);
            if(userDetails==null)
            {
                log.warn(Constants.Messages.noUserFound);
                responseDTO.setMessage(Constants.Messages.noUserFound);
                responseDTO.setStatusCode(Constants.StatusCodes.notFound);
                return responseDTO;
            }

            Chapter chapterDetails=chapterCreationRepository.findByChapterId(chapterId);
            if(chapterDetails==null)
            {
                log.warn(Constants.Messages.chapterNotFound);
                responseDTO.setMessage(Constants.Messages.chapterNotFound);
                responseDTO.setStatusCode(Constants.StatusCodes.noContent);
                return responseDTO;
            }

            chapterDetails.setAdminId(null);

            chapterCreationRepository.save(chapterDetails);

            log.info(Constants.Messages.success);
            responseDTO.setMessage(Constants.Messages.success);
            responseDTO.setStatusCode(Constants.StatusCodes.success);


        }
        catch (Exception e)
        {
            log.error(Constants.Messages.error,e);

            e.printStackTrace();
            responseDTO.setMessage(Constants.Messages.error);
            responseDTO.setStatusCode(Constants.StatusCodes.error);
        }


        return responseDTO;
    }

    public ResponseDTO getAllUsersByChapterId(HttpServletRequest request,String jsonData)
    {
        ResponseDTO responseDTO=new ResponseDTO();
        try {
            if(jsonData==null)
            {
                log.warn(Constants.Messages.emptyRequestBody);
                responseDTO.setMessage(Constants.Messages.emptyRequestBody);
                responseDTO.setStatusCode(Constants.StatusCodes.noContent);
                return responseDTO;
            }

            JSONObject jsonObject=new JSONObject(jsonData);
            Long chapterId= jsonObject.getLong("chapterId");
            Long userId=jsonObject.optLong("userId");

            //User Details
            Usermaster userDetails=usermasterRepository.findByUserId(userId);
            if(userDetails==null)
            {
                log.warn(Constants.Messages.noUserFound);
                return emptyResponseUtil.emptyUserResponse();
            }

            Chapter chapterDetails=chapterCreationRepository.findByChapterId(chapterId);
            if(chapterDetails==null)
            {
                log.warn(Constants.Messages.chapterNotFound);

                return emptyResponseUtil.emptyChapterResponse();
            }

            List<Usermaster> usersUnderChapter = usermasterRepository.findByChapterId(chapterDetails);
            List <ChapterMemberDetailsDTO> members = new ArrayList<>();
            List <ChapterMemberDetailsDTO> pendingMembers = new ArrayList<>();

            for (Usermaster user :usersUnderChapter){
                ChapterMemberDetailsDTO member = new ChapterMemberDetailsDTO();
                member.setUserId(user.getUserId());
                member.setSignUpAs(user.getSignUpTypeId().getSignUpType());

                String profileURL= s3Service.accessFile(user.getProfilePicDir(),user.getProfilePicName());
                member.setProfieLink(profileURL);

                member.setFirstName(user.getFirstName());
                member.setLastName(user.getLastName());
                member.setBio(user.getBio());
                member.setUniversityId(user.getUniversityId().getUniversityId());
                member.setUniversityName(user.getUniversityId().getUniversityName());
                member.setOtherUniversityName(user.getOtherUniversityName());
                member.setMobileNumber(user.getMobileNumber());
                member.setDateOfBirth(user.getDateOfBirth());
                member.setTags(user.getTags());
                member.setGraduationYear(user.getGraduationYear());

                if(user.getMajorId()!=null)
                {
                    member.setMajorId(user.getMajorId().getId());
                    member.setMajorName(user.getMajorId().getNameOfMajor());
                }

                member.setNotificationPreference(user.getNotificationPreference());
                member.setPrivacySettings(user.getPrivacySettings());
                member.setApprovedStatus(user.getApprovedStatus());

                if (user.getApprovedBy() != null){
                    String approvedBy = user.getApprovedBy().getFirstName() + user.getApprovedBy().getLastName();
                    member.setApprovedBy(approvedBy);
                }
                if (user.getRequestedBy() != null) {
                    String requestedBy = user.getRequestedBy().getFirstName() + user.getRequestedBy().getLastName();
                    member.setRequestedBy(requestedBy);
                }
                member.setRequestedOn(user.getRequestedOn());
                member.setRespondedOn(user.getRespondedOn());
                if (user.getApprovedStatus().equals(1)){
                    members.add(member);
                }else if (member.getApprovedStatus().equals(0)){
                    pendingMembers.add(member);
                }
            }
            Map<String, List<ChapterMemberDetailsDTO>> object = new HashMap<>();
            object.put("members", members);
            object.put("pending", pendingMembers);

            responseDTO.setResponseObject(object);
            log.info(Constants.Messages.success);
            responseDTO.setMessage(Constants.Messages.success);
            responseDTO.setStatusCode(Constants.StatusCodes.success);


        }
        catch (Exception e)
        {
            log.error(Constants.Messages.error,e);

            e.printStackTrace();
            responseDTO.setMessage(Constants.Messages.error);
            responseDTO.setStatusCode(Constants.StatusCodes.error);
        }


        return responseDTO;
    }

    public ResponseDTO approveOrRejectChapterMemberRequest(HttpServletRequest request,String jsonData)
    {
        ResponseDTO responseDTO=new ResponseDTO();
        try {
            if(jsonData==null)
            {
                log.warn(Constants.Messages.emptyRequestBody);
                responseDTO.setMessage(Constants.Messages.emptyRequestBody);
                responseDTO.setStatusCode(Constants.StatusCodes.noContent);
                return responseDTO;
            }

            JSONObject jsonObject = new JSONObject(jsonData);
            Long chapterId= jsonObject.getLong("chapterId");
            Long loggedInUserId=jsonObject.optLong("loggedInUserId");
            Long memberUserId = jsonObject.optLong("memberUserId");
            Integer status = jsonObject.optInt("status");

            //User Details
            Usermaster loggedInUserDetails = usermasterRepository.findByUserId(loggedInUserId);
            if(loggedInUserDetails==null)
            {
                log.warn(Constants.Messages.noUserFound);
                responseDTO.setMessage(Constants.Messages.noUserFound);
                responseDTO.setStatusCode(Constants.StatusCodes.notFound);
                return responseDTO;
            }

            Chapter chapterDetails = chapterCreationRepository.findByChapterId(chapterId);

            if(chapterDetails==null)
            {
                log.warn(Constants.Messages.chapterNotFound);
                responseDTO.setMessage(Constants.Messages.chapterNotFound);
                responseDTO.setStatusCode(Constants.StatusCodes.noContent);
                return responseDTO;
            }
            if (!loggedInUserDetails.getSignUpTypeId().getId().equals(4L)) {// Super Admin can approve
                if (!chapterDetails.getAdminId().getUserId().equals(loggedInUserId)){
                    log.warn(Constants.Messages.userDontHaveAccessToApprove);
                    responseDTO.setMessage(Constants.Messages.userDontHaveAccessToApprove);
                    responseDTO.setStatusCode(Constants.StatusCodes.noContent);
                    return responseDTO;
                }
            }
            Usermaster memberUserDetails = usermasterRepository.findByUserId(memberUserId);
            if(memberUserDetails==null)
            {
                log.warn(Constants.Messages.noUserFound);
                responseDTO.setMessage(Constants.Messages.noUserFound);
                responseDTO.setStatusCode(Constants.StatusCodes.notFound);
                return responseDTO;
            }
            memberUserDetails.setApprovedStatus(status);
            memberUserDetails.setApprovedBy(loggedInUserDetails);
            memberUserDetails.setRespondedOn(LocalDateTime.now());

            usermasterRepository.save(memberUserDetails);

            log.info(Constants.Messages.success);
            responseDTO.setMessage(Constants.Messages.success);
            responseDTO.setStatusCode(Constants.StatusCodes.success);


        }
        catch (Exception e)
        {
            log.error(Constants.Messages.error,e);

            e.printStackTrace();
            responseDTO.setMessage(Constants.Messages.error);
            responseDTO.setStatusCode(Constants.StatusCodes.error);
        }


        return responseDTO;
    }
}
