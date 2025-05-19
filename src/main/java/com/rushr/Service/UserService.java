package com.rushr.Service;

import com.rushr.Dto.ResponseDTO;
import com.rushr.Entity.Chapter;
import com.rushr.Entity.Usermaster;
import com.rushr.Repository.ChapterCreationRepository;
import com.rushr.Repository.UsermasterRepository;
import com.rushr.Util.Constants;
import com.rushr.Util.EmptyResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private static final Logger log= LogManager.getLogger(UserService.class);

    @Autowired
    private UsermasterRepository usermasterRepository;

    @Autowired
    private ChapterCreationRepository chapterCreationRepository;

    @Autowired
    private NotificationService notificationService;

    private final EmptyResponseUtil emptyResponseUtil=new EmptyResponseUtil();

    public ResponseDTO sendBidToUser(HttpServletRequest request, String jsonData)
    {
        ResponseDTO responseDTO=new ResponseDTO();
        try {

            if (jsonData == null) {
                log.warn(Constants.Messages.emptyRequestBody);
                responseDTO.setMessage(Constants.Messages.emptyRequestBody);
                responseDTO.setStatusCode(Constants.StatusCodes.noContent);
                return responseDTO;
            }

            JSONObject jsonObject = new JSONObject(jsonData);
            Long userId = jsonObject.optLong("userId");
            Long chapterId = jsonObject.optLong("chapterId");
            String type = jsonObject.getString("type");


            //To get User Details
            Usermaster userDetails = usermasterRepository.findByUserId(userId);

            if(userDetails==null)
            {
                log.warn(Constants.Messages.noUserFound);
                return emptyResponseUtil.emptyUserResponse();
            }


            Chapter chapterDetails = chapterCreationRepository.findByChapterId(chapterId);

            if (chapterDetails == null) {
                log.warn(Constants.Messages.chapterNotFound);
                responseDTO.setMessage(Constants.Messages.chapterNotFound);
                responseDTO.setStatusCode(Constants.StatusCodes.noContent);
                return responseDTO;
            }

            if (type.equalsIgnoreCase("To_Super_Admin"))
            {
                responseDTO=notificationService.sendNotification(request,"Requesting to join this chapter: "+chapterDetails.getChapterName()+" chapter Id: "+chapterDetails.getChapterId(),1L);
            }
            else if(type.equalsIgnoreCase("To_User"))
            {
                return notificationService.sendNotification(request,"Request To Join Our Chapter: "+chapterDetails.getChapterName()+"Chapter Id:" +chapterId,userId);
            }

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

    public ResponseDTO acceptBidRequest(HttpServletRequest request,String jsonData)
    {
        ResponseDTO responseDTO=new ResponseDTO();
        try {
            if(jsonData==null)
            {
                log.info(Constants.Messages.emptyRequestBody);
                responseDTO.setMessage(Constants.Messages.emptyRequestBody);
                responseDTO.setStatusCode(Constants.StatusCodes.noContent);
                return responseDTO;
            }

            JSONObject jsonObject=new JSONObject(jsonData);
            Long userId=jsonObject.optLong("userId");
            String response=jsonObject.getString("response");
            Long chapterId=jsonObject.getLong("chapterId");
            String type=jsonObject.getString("type");

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
                responseDTO.setStatusCode(Constants.StatusCodes.notFound);
                return responseDTO;
            }

            if(response.equalsIgnoreCase("accept"))
            {
                userDetails.setChapterId(chapterDetails);
                usermasterRepository.save(userDetails);
                if(type.equalsIgnoreCase("To_Admin"))
                {
                    notificationService.sendNotification(request,"Bid Accepted",1L);
                }
                else
                {
                    notificationService.sendNotification(request,"Bid Accepted",1L);
                }

            }
            else if(response.equalsIgnoreCase("denied"))
            {
                notificationService.sendNotification(request,"Bid Denied",1L);
            }
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

    public ResponseDTO requestToJoinAsMember(HttpServletRequest request, String jsonData)
    {
        ResponseDTO responseDTO=new ResponseDTO();
        try {

            if (jsonData == null) {
                log.warn(Constants.Messages.emptyRequestBody);
                responseDTO.setMessage(Constants.Messages.emptyRequestBody);
                responseDTO.setStatusCode(Constants.StatusCodes.noContent);
                return responseDTO;
            }

            JSONObject jsonObject = new JSONObject(jsonData);
            Long userId = jsonObject.optLong("userId");
            Long chapterId = jsonObject.optLong("chapterId");

            //To get User Details
            Usermaster userDetails = usermasterRepository.findByUserId(userId);

            if(userDetails==null)
            {
                log.warn(Constants.Messages.noUserFound);
                return emptyResponseUtil.emptyUserResponse();
            }

            Chapter chapterDetails = chapterCreationRepository.findByChapterId(chapterId);

            if (chapterDetails == null) {
                log.warn(Constants.Messages.chapterNotFound);
                responseDTO.setMessage(Constants.Messages.chapterNotFound);
                responseDTO.setStatusCode(Constants.StatusCodes.noContent);
                return responseDTO;
            }
            if (userDetails.getChapterId() != null){
                if (userDetails.getChapterId().getChapterId().equals(chapterId)){
                    if (userDetails.getApprovedStatus().equals(0)){
                        responseDTO.setMessage(Constants.Messages.userMemberRequestAlreadySent);
                        responseDTO.setStatusCode(Constants.StatusCodes.noContent);
                        return  responseDTO;
                    }else if (userDetails.getApprovedStatus().equals(1)){
                        responseDTO.setMessage(Constants.Messages.userMemberOfThisChapter);
                        responseDTO.setStatusCode(Constants.StatusCodes.noContent);
                        return  responseDTO;
                    }
                }else{
                    if (!userDetails.getApprovedStatus().equals(2)){
                        responseDTO.setMessage(Constants.Messages.userMembertOfAnotherChapter);
                        responseDTO.setStatusCode(Constants.StatusCodes.noContent);
                        return  responseDTO;
                    }
                }
            }

            log.info(Constants.Messages.success);
            userDetails.setApprovedStatus(0); // 0 : pending, 1 : approved, 2 : Rejected
            userDetails.setChapterId(chapterDetails);
            userDetails.setRequestedOn(LocalDateTime.now());
            userDetails.setRequestedBy(userDetails);
            userDetails.setApprovedBy(null);
            userDetails.setRespondedOn(null);

            usermasterRepository.save(userDetails);

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
