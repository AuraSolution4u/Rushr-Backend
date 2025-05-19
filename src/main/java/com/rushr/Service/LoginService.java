package com.rushr.Service;

import com.rushr.Dto.AdminDetailDTO;
import com.rushr.Dto.ResponseDTO;
import com.rushr.Dto.UserDetailsRequestDTO;
import com.rushr.Dto.UserDetailsResponseDTO;
import com.rushr.Entity.*;
import com.rushr.Repository.*;
import com.rushr.Util.Constants;
import com.rushr.Util.EmptyResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.support.NullValue;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LoginService {

    private static final Logger log= LogManager.getLogger(LoginService.class);

    @Autowired
    private UsermasterRepository usermasterRepository;

    @Autowired
    private SignUpTypeRepository signUpTypeRepository;

    @Autowired
    private UniversityMasterRepository universityMasterRepository;

    private final EmptyResponseUtil emptyResponseUtil=new EmptyResponseUtil();

    private final SecureRandom secureRandom=new SecureRandom();

    @Autowired
    private OTPGenerationRepository otpGenerationRepository;

    @Autowired
    private MajorListRepository majorListRepository;

    @Autowired
    private GmailService gmailService;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private ChapterCreationRepository chapterCreationRepository;

    private static final int OTP_EXPIRY_IN_MINUTES=10;




    public ResponseDTO userRegistration(HttpServletRequest request, UserDetailsRequestDTO userDetailsRequestDTO)
    {
        ResponseDTO responseDTO=new ResponseDTO();
        try {

            if(userDetailsRequestDTO.getEmailId()==null || userDetailsRequestDTO.getEmailId().isEmpty())
            {
                return emptyResponseUtil.emptyResponse("Empty Email Id");
            }

            //Checking User Already Exists or Not.
            Usermaster usermaster=usermasterRepository.findByEmailId(userDetailsRequestDTO.getEmailId());

            if(usermaster!=null)
            {
                log.info("Already User Exists With Email Id");
                responseDTO.setMessage("Already User Exists With Email Id");
                responseDTO.setStatusCode(Constants.StatusCodes.badRequest);
                return responseDTO;
            }

            if(userDetailsRequestDTO.getFirstName()==null || userDetailsRequestDTO.getFirstName().isEmpty())
            {
                return emptyResponseUtil.emptyResponse("Empty First Name");
            }

            if(userDetailsRequestDTO.getLastName()==null || userDetailsRequestDTO.getLastName().isEmpty())
            {
                return emptyResponseUtil.emptyResponse("Empty Last Name");
            }

            if(userDetailsRequestDTO.getSignUpAs()==null)
            {
                return emptyResponseUtil.emptyResponse("Empty SignUpAs");
            }

            if(userDetailsRequestDTO.getUniversityName()==null)
            {
                return emptyResponseUtil.emptyResponse("Empty University Name");
            }

            if(userDetailsRequestDTO.getPassword()==null || userDetailsRequestDTO.getPassword().isEmpty())
            {
                return emptyResponseUtil.emptyResponse("Empty Password");
            }

            usermaster=new Usermaster();

            usermaster.setEmailId(userDetailsRequestDTO.getEmailId());
            usermaster.setFirstName(userDetailsRequestDTO.getFirstName());
            usermaster.setLastName(userDetailsRequestDTO.getLastName());

            SignUpType signUpType=signUpTypeRepository.findById((long) userDetailsRequestDTO.getSignUpAs());
            usermaster.setSignUpTypeId(signUpType);

            UniversityMaster universityMaster=universityMasterRepository.findByUniversityId(userDetailsRequestDTO.getUniversityName());

            log.info("UniversityId: {}",userDetailsRequestDTO.getUniversityName());

            if(universityMaster.getUniversityName().equalsIgnoreCase("other"))
            {
                usermaster.setUniversityId(universityMaster);
                usermaster.setOtherUniversityName(userDetailsRequestDTO.getOtherUniversityName());
            }
            else
            {
                usermaster.setUniversityId(universityMaster);
            }

            usermaster.setStatus(true);
            usermaster.setPassword(userDetailsRequestDTO.getPassword());
            usermaster.setRegisteredOn(LocalDateTime.now());

            usermasterRepository.save(usermaster);

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


    public ResponseDTO loginIn(HttpServletRequest request,String jsonData)
    {
        ResponseDTO responseDTO=new ResponseDTO();

        try {

            if(jsonData==null || jsonData.isEmpty())
            {
                log.warn(Constants.Messages.emptyRequestBody);

                responseDTO.setMessage(Constants.Messages.emptyRequestBody);
                responseDTO.setStatusCode(Constants.StatusCodes.noContent);
                return responseDTO;
            }

            JSONObject jsonObject=new JSONObject(jsonData);
            String emailId=jsonObject.optString("emailId");
            String password=jsonObject.optString("password");

            if(emailId==null || emailId.isEmpty())
            {
                return emptyResponseUtil.emptyResponse("Empty EmailId");
            }

            if(password==null || password.isEmpty())
            {
                return emptyResponseUtil.emptyResponse("Empty Password");
            }

            Usermaster usermaster=usermasterRepository.findByEmailId(emailId);

            if(usermaster==null)
            {
                log.warn(Constants.Messages.noUserFound);
                responseDTO.setMessage(Constants.Messages.noUserFound);
                responseDTO.setStatusCode(Constants.StatusCodes.notFound);
                return responseDTO;
            }

            if(password.equals(usermaster.getPassword()))
            {

                //To Get User Details.
                UserDetailsResponseDTO dto=userDetails(usermaster);

                log.info(Constants.Messages.success);
                responseDTO.setMessage(Constants.Messages.success);
                responseDTO.setStatusCode(Constants.StatusCodes.success);
                responseDTO.setResponseObject(dto);
            }
            else
            {
                log.warn(Constants.Messages.invalidPassword);
                responseDTO.setMessage(Constants.Messages.invalidPassword);
                responseDTO.setStatusCode(Constants.StatusCodes.unAuthorized);
                return responseDTO;
            }

        }
        catch (Exception e)
        {
            log.error(Constants.Messages.error ,e);

            e.printStackTrace();
            responseDTO.setMessage(Constants.Messages.error);
            responseDTO.setStatusCode(Constants.StatusCodes.error);
        }
        return responseDTO;
    }



    public ResponseDTO sendOTPToEmail(HttpServletRequest request,String jsonData)
    {
        ResponseDTO responseDTO=new ResponseDTO();
        String otp=null;
        Map<String,String> res=new HashMap<>();
        try {
            if(jsonData==null || jsonData.isEmpty())
            {
                log.warn(Constants.Messages.emptyRequestBody);
                responseDTO.setMessage(Constants.Messages.emptyRequestBody);
                responseDTO.setStatusCode(Constants.StatusCodes.noContent);
                return responseDTO;
            }
            JSONObject jsonObject=new JSONObject(jsonData);
            String emailId=jsonObject.optString("emailId");
            String requestType=jsonObject.optString("requestType");

            if(emailId==null || emailId.isEmpty())
            {
                log.warn(Constants.Messages.emptyEmailId);
                return emptyResponseUtil.emptyResponse(Constants.Messages.emptyEmailId);
            }


            OTPGeneration otpGeneration=otpGenerationRepository.findByEmailId(emailId);

            if(otpGeneration!=null && (LocalDateTime.now().isBefore(otpGeneration.getExpiryTime())) && otpGeneration.isStatus())
            {
                log.info("Active OTP Exists");
                otp=otpGeneration.getOtp();
                res.put("emailId",emailId);
                res.put("otp",otp);

            }
            else
            {
                otp=String.format("%06d",secureRandom.nextInt(999999));
                if(otpGeneration==null)
                {
                    log.info("New OTP Generation");
                    OTPGeneration newOTP=new OTPGeneration();
                    newOTP.setOtp(otp);
                    newOTP.setGeneratedTime(LocalDateTime.now());
                    newOTP.setExpiryTime(LocalDateTime.now().plusMinutes(OTP_EXPIRY_IN_MINUTES));
                    newOTP.setEmailId(emailId);
                    newOTP.setStatus(true);
                    otpGenerationRepository.save(newOTP);
                }
                else
                {
                    log.info("InActive OTP");
                    otpGeneration.setOtp(otp);
                    otpGeneration.setGeneratedTime(LocalDateTime.now());
                    otpGeneration.setExpiryTime(LocalDateTime.now().plusMinutes(OTP_EXPIRY_IN_MINUTES));
                    otpGeneration.setStatus(true);
                    otpGenerationRepository.save(otpGeneration);
                }
            }

            gmailService.sendEmail(emailId,otp,requestType);


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

    public ResponseDTO validateOTP(HttpServletRequest request, String jsonData)
    {
        ResponseDTO responseDTO=new ResponseDTO();
        try {
            if(jsonData==null)
            {
                log.warn(Constants.Messages.emptyRequestBody);
                responseDTO.setMessage(Constants.Messages.emptyRequestBody);
                responseDTO.setStatusCode(Constants.StatusCodes.notFound);
                return responseDTO;
            }

            JSONObject jsonObject=new JSONObject(jsonData);
            String emailId=jsonObject.optString("emailId");
            String otp=jsonObject.optString("otp");

            if(emailId==null && emailId.isEmpty())
            {
                log.warn(Constants.Messages.emptyEmailId);
                return emptyResponseUtil.emptyResponse(Constants.Messages.emptyEmailId);
            }
            if(otp==null || otp.isEmpty())
            {
             log.warn("Empty OTP");
             return emptyResponseUtil.emptyResponse("Empty OTP");
            }


            OTPGeneration otpGeneration=otpGenerationRepository.findByEmailId(emailId);

            if(otpGeneration==null)
            {
                log.warn(Constants.Messages.noOtpFound);
                responseDTO.setMessage(Constants.Messages.noOtpFound);
                responseDTO.setStatusCode(Constants.StatusCodes.noContent);
                return responseDTO;
            }

            if(otp.equals(otpGeneration.getOtp()))
            {
                if(LocalDateTime.now().isBefore(otpGeneration.getExpiryTime()) && otpGeneration.isStatus())
                {
                    otpGeneration.setStatus(false);
                    otpGenerationRepository.save(otpGeneration);
                    log.info(Constants.Messages.success);
                    responseDTO.setMessage(Constants.Messages.success);
                    responseDTO.setStatusCode(Constants.StatusCodes.success);
                }
                else
                {
                    log.warn(Constants.Messages.otpExpired);
                    responseDTO.setMessage(Constants.Messages.otpExpired);
                    responseDTO.setStatusCode(Constants.StatusCodes.invalidOtp);
                }
            }
            else
            {
                log.warn(Constants.Messages.invalidOTP);
                responseDTO.setMessage(Constants.Messages.invalidOTP);
                responseDTO.setStatusCode(Constants.StatusCodes.unAuthorized);
            }


        }
        catch (Exception e)
        {
            log.warn(Constants.Messages.error);

            e.printStackTrace();
            responseDTO.setMessage(Constants.Messages.error);
            responseDTO.setStatusCode(Constants.StatusCodes.error);
        }

        return responseDTO;
    }

    public ResponseDTO resetPassword(HttpServletRequest request,String jsonData)
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
            String emailId = jsonObject.optString("emailId");
            String password = jsonObject.optString("password");

            if(emailId==null || emailId.isEmpty())
            {
                return emptyResponseUtil.emptyResponse(Constants.Messages.emptyEmailId);
            }

            if(password==null ||password.isEmpty())
            {
                return emptyResponseUtil.emptyResponse("Empty Password");
            }

            Usermaster usermaster = usermasterRepository.findByEmailId(emailId);
            if (usermaster == null)
            {
                log.warn(Constants.Messages.noUserFound);
                responseDTO.setMessage(Constants.Messages.noUserFound);
                responseDTO.setStatusCode(Constants.StatusCodes.notFound);
                return responseDTO;
            }

            usermaster.setPassword(password);

            usermasterRepository.save(usermaster);

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

    public ResponseDTO updateProfile(HttpServletRequest request, UserDetailsRequestDTO userDetailsRequestDTO)
    {
        ResponseDTO responseDTO=new ResponseDTO();
        try {
            Usermaster usermaster=usermasterRepository.findByEmailId(userDetailsRequestDTO.getEmailId());

            if(usermaster==null)
            {
                log.warn(Constants.Messages.noUserFound);
                responseDTO.setMessage(Constants.Messages.noUserFound);
                responseDTO.setStatusCode(Constants.StatusCodes.notFound);
                return responseDTO;
            }


            if(userDetailsRequestDTO.getFirstName()==null || userDetailsRequestDTO.getFirstName().isEmpty())
            {
                return emptyResponseUtil.emptyResponse("Empty First Name");
            }

            if(userDetailsRequestDTO.getLastName()==null || userDetailsRequestDTO.getLastName().isEmpty())
            {
                return emptyResponseUtil.emptyResponse("Empty Last Name");
            }

            if(userDetailsRequestDTO.getDateOfBirth()==null)
            {
                return emptyResponseUtil.emptyResponse("Empty Date Of Birth");
            }

            if(userDetailsRequestDTO.getBio()==null || userDetailsRequestDTO.getBio().isEmpty())
            {
                return emptyResponseUtil.emptyResponse("Empty Bio");
            }

            if(userDetailsRequestDTO.getUniversityName()==null)
            {
                return emptyResponseUtil.emptyResponse("Empty University Name");
            }
            
            if(userDetailsRequestDTO.getGraduationYear()==null)
            {
                return emptyResponseUtil.emptyResponse("Empty Graduation Year");
            }

            if(userDetailsRequestDTO.getNotificationPreference()==null)
            {
                return emptyResponseUtil.emptyResponse("Empty Notification Preference");
            }

            if(userDetailsRequestDTO.getPrivacySettings()==null)
            {
                return emptyResponseUtil.emptyResponse("Empty Privacy Settings");
            }

            SignUpType signUpType=signUpTypeRepository.findById((long)userDetailsRequestDTO.getSignUpAs());

            usermaster.setSignUpTypeId(signUpType);
            usermaster.setDateOfBirth(userDetailsRequestDTO.getDateOfBirth());
            usermaster.setFirstName(userDetailsRequestDTO.getFirstName());
            usermaster.setLastName(userDetailsRequestDTO.getLastName());
            if(userDetailsRequestDTO.getMobileNumber()==null)
            {
                usermaster.setMobileNumber(null);
            }
            else
            {
                usermaster.setMobileNumber(userDetailsRequestDTO.getMobileNumber());
            }

            usermaster.setTags(userDetailsRequestDTO.getTags());

            UniversityMaster universityMaster=universityMasterRepository.findByUniversityId(userDetailsRequestDTO.getUniversityName());
            usermaster.setUniversityId(universityMaster);

            if(userDetailsRequestDTO.getUniversityName()==5)
            {
                usermaster.setOtherUniversityName(userDetailsRequestDTO.getOtherUniversityName());
            }


            usermaster.setGraduationYear(userDetailsRequestDTO.getGraduationYear());

            if(userDetailsRequestDTO.getMajor()==null)
            {
                usermaster.setMajorId(null);
            }
            else
            {
                MajorsListMaster majorsListMaster=majorListRepository.findById((long)userDetailsRequestDTO.getMajor());
                usermaster.setMajorId(majorsListMaster);
            }

            usermaster.setNotificationPreference(userDetailsRequestDTO.getNotificationPreference());
            usermaster.setPrivacySettings(userDetailsRequestDTO.getPrivacySettings());

            usermaster.setBio(userDetailsRequestDTO.getBio());

            if (userDetailsRequestDTO.getProfilePicDir() != null) {
                usermaster.setProfilePicDir(userDetailsRequestDTO.getProfilePicDir());
            } else {
                usermaster.setProfilePicDir(null);
            }

            if(userDetailsRequestDTO.getProfilePicName()!=null)
            {
                usermaster.setProfilePicName(userDetailsRequestDTO.getProfilePicName());
            }
            else
            {
                usermaster.setProfilePicName(null);
            }

            usermasterRepository.save(usermaster);

            // To Get User Details.
            UserDetailsResponseDTO dto=userDetails(usermaster);

            responseDTO.setMessage(Constants.Messages.success);
            responseDTO.setStatusCode(Constants.StatusCodes.success);
            responseDTO.setResponseObject(dto);
            return responseDTO;
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

    public ResponseDTO getAllUsers(String jsonData)
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
            int userType=jsonObject.getInt("userType");
            Long chapterId=jsonObject.optLong("chapterId");

            // UserType 0 All Users & 1 Unassigned Users

            Usermaster usermaster=usermasterRepository.findByUserId(userId);

            if(usermaster==null)
            {
                log.warn(Constants.Messages.noUserFound);
                return emptyResponseUtil.emptyUserResponse();
            }

            List<UserDetailsResponseDTO> usersList=new ArrayList<>();
            if(userType==0)
            {
                if(usermaster.getSignUpTypeId().getId()==4)
                {
                    //Using Streams
                    usersList.addAll((usermasterRepository.findAll()).stream().filter((dt)->dt.getSignUpTypeId().getId()!=4).map(this::userDetails).toList());
                }
                else
                {
                    log.warn(Constants.Messages.unAuthorized);
                    responseDTO.setMessage(Constants.Messages.unAuthorized);
                    responseDTO.setStatusCode(Constants.StatusCodes.unAuthorized);
                }
            }
            else if(userType==1)
            {
                Chapter chapterDetails=chapterCreationRepository.findByChapterId(chapterId);

                if (chapterDetails == null){
                    log.warn(Constants.Messages.chapterNotFound);
                    responseDTO.setMessage(Constants.Messages.chapterNotFound);
                    responseDTO.setStatusCode(Constants.StatusCodes.noContent);
                    return responseDTO;
                }

                usersList.addAll((usermasterRepository.findAll()).stream().filter(val->(val.getChapterId()==null || val.getChapterId().getChapterId().equals(chapterId)) && val.getSignUpTypeId().getId()!=4 && val.getSignUpTypeId().getId()!=2 ).map(this::userDetails).toList());
            }

            log.info(Constants.Messages.success);
            responseDTO.setMessage(Constants.Messages.success);
            responseDTO.setStatusCode(Constants.StatusCodes.success);
            responseDTO.setResponseObject(usersList);


        }
        catch (Exception e)
        {
            log.error(Constants.StatusCodes.error);
            e.printStackTrace();
            responseDTO.setMessage(Constants.Messages.error);
            responseDTO.setStatusCode(Constants.StatusCodes.error);
        }
        return responseDTO;
    }


    //To Get User Details
    public UserDetailsResponseDTO userDetails(Usermaster usermaster)
    {
        UserDetailsResponseDTO dto=new UserDetailsResponseDTO();
        dto.setEmailId(usermaster.getEmailId());
        dto.setUserId(usermaster.getUserId());
        dto.setFirstName(usermaster.getFirstName());
        dto.setLastName(usermaster.getLastName());
        dto.setSignUpAs(usermaster.getSignUpTypeId().getSignUpType());

        log.info("University ID: {}",usermaster.getUniversityId());
        if(usermaster.getUniversityId()!=null)
        {
            log.info("1");
            // Getting Details -- Already Registered User
            if(usermaster.getUniversityId().getUniversityId()==48)
            {
                log.info("2");
                dto.setUniversityId(usermaster.getUniversityId().getUniversityId());
                dto.setOtherUniversityName(usermaster.getOtherUniversityName());
            }
            else
            {
                log.info("3");
                dto.setUniversityId(usermaster.getUniversityId().getUniversityId());
                dto.setUniversityName(usermaster.getUniversityId().getUniversityName());
            }
        }


        // To get Profile Pic Details
        if(usermaster.getProfilePicDir()!=null && usermaster.getProfilePicName()!=null)
        {
            String profileURL= s3Service.accessFile(usermaster.getProfilePicDir(),usermaster.getProfilePicName());
            dto.setProfieLink(profileURL);
        }

        if(usermaster.getMobileNumber()!=null)
        {
            dto.setMobileNumber(usermaster.getMobileNumber());
        }

        if(usermaster.getBio()!=null)
        {
            dto.setBio(usermaster.getBio());
        }

        if(usermaster.getDateOfBirth()!=null)
        {
            dto.setDateOfBirth(usermaster.getDateOfBirth());
        }

        if(usermaster.getTags()!=null)
        {
            dto.setTags(usermaster.getTags());
        }

        if(usermaster.getGraduationYear()!=null)
        {
            dto.setGraduationYear(usermaster.getGraduationYear());
        }

        if(usermaster.getMajorId()!=null)
        {
            dto.setMajorId(usermaster.getMajorId().getId());
            dto.setMajorName(usermaster.getMajorId().getNameOfMajor());
        }

        if(usermaster.getNotificationPreference()!=null)
        {
            dto.setNotificationPreference(usermaster.getNotificationPreference());
        }

        if(usermaster.getPrivacySettings()!=null)
        {
            dto.setPrivacySettings(usermaster.getPrivacySettings());
        }

        dto.setApprovedStatus(usermaster.getApprovedStatus());
        log.info("Login Service chapterId: {}",usermaster.getChapterId());
        if(usermaster.getChapterId()!=null)
        {
            Chapter chapterDetails=chapterCreationRepository.findByChapterId(usermaster.getChapterId().getChapterId());
            if(chapterDetails!=null)
            {
                List <Usermaster> members = usermasterRepository.findByChapterId(chapterDetails);
                List <Long> memberIds = new ArrayList<>();
                if (members != null){
                    for (Usermaster user:members){
                        if (user.getApprovedStatus().equals(1)){
                            memberIds.add(user.getUserId());
                        }
                    }
                }
                chapterDetails.setMembers(memberIds);
                dto.setChapterDetails(chapterService.getChapterDetails(chapterDetails));
            }

            if(usermaster.getChapterId().getAdminId()!=null)
            {
                if(usermaster.getChapterId().getAdminId().getUserId().equals(usermaster.getUserId()))
                {
                    dto.setAdmin(true);
                }
            }
        }
        return dto;
    }



}
