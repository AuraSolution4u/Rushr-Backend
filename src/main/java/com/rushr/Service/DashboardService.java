package com.rushr.Service;

import com.rushr.Dto.ResponseDTO;
import com.rushr.Entity.GreekAlphabetsMaster;
import com.rushr.Entity.MajorsListMaster;
import com.rushr.Entity.SignUpType;
import com.rushr.Entity.UniversityMaster;
import com.rushr.Repository.GreekAlphabetsMasterRepository;
import com.rushr.Repository.MajorListRepository;
import com.rushr.Repository.SignUpTypeRepository;
import com.rushr.Repository.UniversityMasterRepository;
import com.rushr.Util.Constants;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    public static final Logger log= LogManager.getLogger(DashboardService.class);

    @Autowired
    private UniversityMasterRepository universityMasterRepository;

    @Autowired
    private MajorListRepository majorListRepository;

    @Autowired
    private GreekAlphabetsMasterRepository greekAlphabetsMasterRepository;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private SignUpTypeRepository signUpTypeRepository;


    public ResponseDTO getMasterData(HttpServletRequest request)
    {
        ResponseDTO responseDTO=new ResponseDTO();
        Map<String,Object> masterList=new HashMap<>();
        try {

            //SignUp Types
            List<SignUpType> signUpTypeList=signUpTypeRepository.findAll();
            masterList.put("signUpTypeList",signUpTypeList);

            //University List
            List<UniversityMaster> universityMasterList=universityMasterRepository.findAll();
            masterList.put("universityMasterList",universityMasterList);

            //Major List
            List<MajorsListMaster> majorsListMasterList=majorListRepository.findAll();
            masterList.put("majorMasterList",majorsListMasterList);


            //Greek Alphabets Images
//            List<Map<String,String>> greekAlphabetImagesList=new ArrayList<>();
//
//            List<GreekAlphabetsMaster> greekAlphabetsMasterList=greekAlphabetsMasterRepository.findAll();
//
//            for(GreekAlphabetsMaster list:greekAlphabetsMasterList)
//            {
//                Map<String,String> details=new HashMap<>();
//                details.put("greekId", String.valueOf(list.getGreekAlphabetId()));
//                details.put("Greek_Alphabet_Name",list.getGreekAlphabetName());
//                details.put("url",s3Service.accessFile(list.getDirectory(),list.getGreekAlphabetName()+".png"));
//                greekAlphabetImagesList.add(details);
//            }
//
//            masterList.put("greekAlphabetImagesList",greekAlphabetImagesList);

            responseDTO.setMessage(Constants.Messages.success);
            responseDTO.setStatusCode(Constants.StatusCodes.success);
            responseDTO.setResponseObject(masterList);




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
}
