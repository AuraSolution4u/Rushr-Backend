package com.rushr.Controller;

import com.rushr.Dto.EmailDetailsDTO;
import com.rushr.Dto.ResponseDTO;
import com.rushr.Service.S3Service;
import com.rushr.Service.GmailService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/restAPI/test")
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class TestController {

    @Autowired
    private GmailService gmailService;

    @Autowired
    private S3Service s3Service;

//
//    @PostMapping(value = "/triggerEmail")
//    public ResponseDTO triggerEmail(@RequestBody EmailDetailsDTO emailDetailsDTO)
//    {
//        return gmailService.sendEmail(emailDetailsDTO);
//    }

//    @PostMapping(value = "/triggerEmailBody")
//    public ResponseDTO emailBody(@RequestBody EmailDetailsDTO emailDetailsDTO)
//    {
//        return gmailService.sendEmailWithHTMLBody(emailDetailsDTO);
//    }



}
