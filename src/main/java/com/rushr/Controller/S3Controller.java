package com.rushr.Controller;

import com.rushr.Dto.ResponseDTO;
import com.rushr.Service.S3Service;
import com.rushr.Util.Constants;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/restAPI/AWSS3")
@CrossOrigin(allowedHeaders = "*",origins = "*")
public class S3Controller {

    private static final Logger log= LogManager.getLogger(S3Controller.class);

    @Autowired
    private S3Service s3Service;

    @PostMapping(value = "/uploadFile")
    public ResponseDTO uploadFile(HttpServletRequest request, @RequestParam(value = "file") MultipartFile file,@RequestParam(value = "directory") String directory,@RequestParam(value = "userId") Long userId)
    {
        return s3Service.uploadFile(request,file,directory,userId);
    }

    @PostMapping(value = "/accessFile")
    public ResponseDTO accessFile(HttpServletRequest request, @RequestBody String jsonData)
    {
        ResponseDTO responseDTO=new ResponseDTO();
        JSONObject jsonObject=new JSONObject(jsonData);
        String directory=jsonObject.optString("directory");
        String fileName=jsonObject.optString("fileName");

        String url=s3Service.accessFile(directory,fileName);

        Map<String,String> res=new HashMap<>();
        res.put("url",url);
        responseDTO.setMessage(Constants.Messages.success);
        responseDTO.setStatusCode(Constants.StatusCodes.success);
        responseDTO.setResponseObject(res);

        return responseDTO;
    }

    @PostMapping(value = "/deleteFile")
    public ResponseDTO deleteFile(HttpServletRequest request,@RequestBody String jsonData)
    {
        ResponseDTO responseDTO=new ResponseDTO();
        try {

            boolean response=s3Service.deleteFile(request,jsonData);

            if(response)
            {
                log.info(Constants.Messages.success);
                responseDTO.setMessage(Constants.Messages.success);
                responseDTO.setStatusCode(Constants.StatusCodes.success);
            }
        }
        catch (Exception e)
        {
            log.error(Constants.Messages.error+" "+e);
            e.printStackTrace();
            responseDTO.setMessage(Constants.Messages.error);
            responseDTO.setStatusCode(Constants.StatusCodes.error);
        }
        return responseDTO;
    }

    @PostMapping(value = "/downloadFile")
    public ResponseDTO downloadFile(HttpServletRequest request,@RequestBody String jsonData)
    {
        return s3Service.downloadFile(request,jsonData);
    }

}
