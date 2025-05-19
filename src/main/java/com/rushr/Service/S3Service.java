package com.rushr.Service;

import com.rushr.Dto.ResponseDTO;
import com.rushr.Entity.Usermaster;
import com.rushr.Repository.UsermasterRepository;
import com.rushr.Util.Constants;
import com.rushr.Util.EmptyResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.internal.resource.S3ResourceType;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class S3Service {

    private static final Logger log= LogManager.getLogger(S3Service.class);

    private final S3Client s3Client;

    private final S3Presigner s3Presigner;


    public S3Service(S3Client s3Client, S3Presigner s3Presigner) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
    }

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.bucketName}")
    private String bucketName;

    @Autowired
    private UsermasterRepository usermasterRepository;

    private EmptyResponseUtil emptyResponseUtil=new EmptyResponseUtil();

    private static final int EXPIRY_TIME=1440;

    public ResponseDTO uploadFile(HttpServletRequest request, MultipartFile file,String directory,Long userId)
    {
        ResponseDTO responseDTO=new ResponseDTO();
        try
        {
            if(userId==null)
            {
                log.warn("Empty UserId");
               responseDTO.setMessage("Empty UserId");
               responseDTO.setStatusCode(Constants.StatusCodes.noContent);
               return responseDTO;
            }

            Usermaster usermaster=usermasterRepository.findByUserId(userId);

            if(usermaster==null)
            {
                log.warn(Constants.Messages.noUserFound);
                responseDTO.setMessage(Constants.Messages.noUserFound);
                responseDTO.setStatusCode(Constants.StatusCodes.notFound);
                return responseDTO;
            }


            log.info(Constants.Messages.success);

            //To get Uploaded File Details
            Map<String,String> res=getFileDetails(file,directory,userId);

            responseDTO.setMessage(Constants.Messages.success);
            responseDTO.setStatusCode(Constants.StatusCodes.success);
            responseDTO.setResponseObject(res);

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

    public Map<String,String> getFileDetails(MultipartFile file,String directory,Long userId) throws IOException
    {
        //Main Logic to Upload A File to S3 Bucket
        Map<String,String> fileDetails=new HashMap<>();

        File tempFile=File.createTempFile("upload-",file.getOriginalFilename());
        file.transferTo(tempFile);

        String fileName=System.currentTimeMillis()+"_"+userId+"_"+file.getOriginalFilename();

        String key=directory+"/"+fileName;
//
//        S3TransferManager s3TransferManager=S3TransferManager.create();
//        UploadFileRequest uploadFileRequest= UploadFileRequest.builder().putObjectRequest(PutObjectRequest..builder().build())

        s3Client.putObject(PutObjectRequest
                                   .builder()
                                   .bucket(bucketName)
                                   .key(key)
                                   .build()
                ,RequestBody.fromFile(tempFile));

        tempFile.delete();


        log.info("File Uploaded Successfully");

        fileDetails.put("directory",directory);
        fileDetails.put("fileName",fileName);


        return fileDetails;
    }


    public boolean deleteFile(HttpServletRequest request,String jsonData)
    {
        ResponseDTO responseDTO=new ResponseDTO();
        try {

            JSONObject jsonObject=new JSONObject(jsonData);
            String directory=jsonObject.optString("directory");
            String fileName=jsonObject.optString("fileName");

            String key=directory+"/"+fileName;
            DeleteObjectRequest deleteObjectRequest= DeleteObjectRequest
                                                             .builder()
                                                             .bucket(bucketName)
                                                             .key(key)
                                                             .build();

            DeleteObjectResponse deleteObjectResponse=s3Client.deleteObject(deleteObjectRequest);

            return true;


        }
        catch (Exception e)
        {
            log.error(Constants.Messages.error);

            e.printStackTrace();
            responseDTO.setMessage(Constants.Messages.error);
            responseDTO.setStatusCode(Constants.StatusCodes.error);
        }
        return false;
    }

    public ResponseDTO downloadFile(HttpServletRequest request,String jsonData)
    {
        ResponseDTO responseDTO=new ResponseDTO();
        try {

            JSONObject jsonObject=new JSONObject(jsonData);
            String directory=jsonObject.optString("directory");
            String fileName=jsonObject.optString("fileName");

            if(directory==null)
            {
                return emptyResponseUtil.emptyResponse("Empty Category Value");
            }

            if(fileName==null)
            {
                return emptyResponseUtil.emptyResponse("Empty Filename");
            }

            //Unique Key to represent Objects in AWS S3
            String key=directory+"/"+fileName;

            // Download Files to specific path
            String dir="C:/rushr";

            Path destination=Path.of(dir+"/"+fileName);

            log.info("Dest: {}",destination);

            s3Client.getObject(GetObjectRequest
                                       .builder()
                                       .bucket(bucketName)
                                       .key(key)
                                       .build()
                    ,destination);

            log.info(Constants.Messages.success);
            responseDTO.setMessage(Constants.Messages.success);
            responseDTO.setStatusCode(Constants.StatusCodes.success);

        }
        catch (Exception e)
        {
            log.error(Constants.Messages.error+": "+e);

            e.printStackTrace();
            responseDTO.setMessage(Constants.Messages.error);
            responseDTO.setStatusCode(Constants.StatusCodes.error);
        }
        return responseDTO;
    }

    public String accessFile(String directory,String fileName)
    {
        String finalURL=null;
        try {

            String key=directory+"/"+fileName;



            //Getting specific Object based on key
            GetObjectRequest getObjectRequest=GetObjectRequest
                                                      .builder()
                                                      .bucket(bucketName)
                                                      .key(key)
                                                      .build();

            //Setting Expiration Time for URL
            GetObjectPresignRequest preSignRequest=GetObjectPresignRequest
                                                           .builder()
                                                           .signatureDuration(Duration.ofMinutes(EXPIRY_TIME))
                                                           .getObjectRequest(getObjectRequest)
                                                           .build();

            URL presignedURL=s3Presigner.presignGetObject(preSignRequest).url();
             finalURL= String.valueOf(presignedURL);
        }
        catch (Exception e)
        {
            log.error(Constants.Messages.error+": "+e);

            e.printStackTrace();
        }
        return finalURL;
    }



}
