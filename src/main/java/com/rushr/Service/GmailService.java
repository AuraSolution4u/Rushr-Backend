package com.rushr.Service;

import com.rushr.Dto.EmailDetailsDTO;
import com.rushr.Dto.ResponseDTO;
import com.rushr.Util.Constants;
import jakarta.mail.internet.MimeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class GmailService {

    private static final Logger log= LogManager.getLogger(GmailService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Autowired
    private S3Service s3Service;

    private SecureRandom secureRandom=new SecureRandom();

    private static final String subjectSignUp="Your One-Time Password (OTP) for RushR Sign-Up";

    private static final String subjectResetPassword="Your One-Time Password (OTP) to reset your RushR account password";

    public ResponseDTO sendEmail(EmailDetailsDTO emailDetailsDTO)
    {
        ResponseDTO responseDTO=new ResponseDTO();

        try
        {
            SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
            simpleMailMessage.setFrom(senderEmail);
            simpleMailMessage.setTo(emailDetailsDTO.getRecipient());
            simpleMailMessage.setText(emailDetailsDTO.getMsgBody());
            simpleMailMessage.setSubject(emailDetailsDTO.getSubject());

            javaMailSender.send(simpleMailMessage);

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
    public ResponseDTO sendEmail(String recipient, String otp, String requestType)
    {
        ResponseDTO responseDTO=new ResponseDTO();
        try {
            MimeMessage message=javaMailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(message,true);

            helper.setTo(recipient);



            //To get Rushr logo from amazon s3
            String url= s3Service.accessFile("projectFiles","logo.png");


//            log.info("Image URL: {}",url);

            if ((requestType.equalsIgnoreCase("signup"))) {
                helper.setSubject(subjectSignUp);

                log.info("HTML TEMPLATE {}",htmlBodySignUp(otp,url));
                helper.setText(htmlBodySignUp(otp,url),true);

            }
            else if(requestType.equalsIgnoreCase("reset password") ) {
                helper.setSubject(subjectResetPassword);
                helper.setText(htmlBodyForResetPassword(otp,url),true);
            }

            javaMailSender.send(message);

            responseDTO.setMessage(Constants.Messages.success);
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

    public ResponseDTO sendEmailWithHTMLBody(EmailDetailsDTO emailDetailsDTO)
    {
        ResponseDTO responseDTO=new ResponseDTO();
        try {
            MimeMessage message=javaMailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(message,true);
            helper.setTo(emailDetailsDTO.getRecipient());
            helper.setSubject(emailDetailsDTO.getSubject());


            String otp=String.format("%06d",secureRandom.nextInt(666666));
            String text= htmlBodySignUp(otp);

            helper.setText(text,true);

            javaMailSender.send(message);

            responseDTO.setMessage(Constants.Messages.success);
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


    public String htmlBodySignUp(String otp,String imageUrl)
    {
        String htmlTemplate = """
                <!DOCTYPE html>
                               <html lang="en">
                               
                               <head>
                                   <meta charset="UTF-8">
                                   <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                   <title>Welcome to RushR</title>
                                   <style>
                                       body {
                                           font-family: Arial, sans-serif !important;
                                           background-color: #f9f9f9 !important;
                                           margin: 0 !important;
                                           padding: 0 !important;
                                       }
                               
                                       .email-container {
                                           max-width: 600px !important;
                                           margin: 20px auto !important;
                                           background-color: #ffffff !important;
                                           border: 1px solid #e0e0e0 !important;
                                           border-radius: 8px !important;
                                           overflow: hidden !important;
                                       }
                               
                                       .header h1 {
                                           margin: 0 !important;
                                           font-size: 24px !important;
                                       }
                               
                                       .content {
                                           padding: 20px !important;
                                           color: #333333 !important;
                                       }
                               
                                       .content h2 {
                                           font-size: 20px !important;
                                           margin-top: 0 !important;
                                       }
                               
                                       .content p {
                                           line-height: 1.6 !important;
                                       }
                               
                                       .otp {
                                           font-size: 18px !important;
                                           color: #ff0000 !important;
                                           font-weight: bold !important;
                                           text-align: center !important;
                                           margin: 20px 0 !important;
                                       }
                               
                                       .footer {
                                           background-color: #f1f1f1 !important;
                                           text-align: center !important;
                                           padding: 10px !important;
                                           font-size: 14px !important;
                                           color: #666666 !important;
                                       }
                               
                                       .footer a {
                                           color: #007bff !important;
                                           text-decoration: none !important;
                                       }
                               
                                       .footer a:hover {
                                           text-decoration: underline !important;
                                       }
                               
                                       img {
                                           width: 100px !important;
                                           height: auto !important;
                                           margin: 10px auto !important;
                                           display: block !important;
                                       }
                                   </style>
                               </head>
                               
                               <body>
                               <div class="email-container">
                                   <div class="content">
                                       <h2>Hi,</h2>
                                       <h2>Welcome to RUSHR! We're thrilled to have you join our community!</h2>
                                       <p>To complete your sign-up, please use the following One-Time Password (OTP):</p>
                                       <div class="otp">[Your OTP Code]</div>
                                       <p>This code is valid for the next 10 minutes. Please enter it on the app to verify your account.</p>
                                       <p>If you didn’t request this, please disregard this email.</p>
                                       <p>Feel free to reach out to our support team at
                                           <a href="mailto:support@rushr.org" style="color: #ff0000 !important; text-decoration: none !important;">support@rushr.org</a>
                                           if you have any questions.</p>
                                       <p>Thank you for choosing RushR!</p>
                                   </div>
                                   <div class="footer">
                                       <p>Best Regards,<br>RushR Team</p>
                                   </div>
                               
                                   <img src="[URL]" alt="RushR Logo">
                               </div>
                               </body>
                               
                               </html>
                               
                        
                """;
        return htmlTemplate.replace("[Your OTP Code]",otp).replace("[URL]",imageUrl);
    }

//log.info(Constants.Messages.success);
    public String htmlBodyForResetPassword(String otp,String imageUrl)
    {
        String template= """
                <!DOCTYPE html>
                       <html lang="en">
                       <head>
                           <meta charset="UTF-8">
                           <meta name="viewport" content="width=device-width, initial-scale=1.0">
                           <title>Reset Your Password</title>
                           <style>
                               body {
                                   font-family: Arial, sans-serif;
                                   background-color: #ffffff !important;
                                   color: #333333 !important;
                                   margin: 0;
                                   padding: 0;
                               }
                       
                               .email-container {
                                   max-width: 600px;
                                   margin: 20px auto;
                                   background-color: #ffffff;
                                   border: 1px solid #e0e0e0;
                                   border-radius: 8px;
                                   overflow: hidden;
                               }
                       
                               .content {
                                   padding: 20px;
                                   color: #333333 !important;
                               }
                       
                               .content h2 {
                                   font-size: 20px;
                                   margin-top: 0;
                                   color: #333333 !important;
                               }
                       
                               .content p {
                                   line-height: 1.6;
                                   color: #333333 !important;
                               }
                       
                               .otp {
                                   font-size: 18px;
                                   color: #ff0000 !important;
                                   font-weight: bold;
                                   text-align: center;
                                   margin: 20px 0;
                               }
                       
                               .footer {
                                   background-color: #f1f1f1;
                                   text-align: center;
                                   padding: 10px;
                                   font-size: 14px;
                                   color: #666666 !important;
                               }
                       
                               .footer a {
                                   color: #007bff !important;
                                   text-decoration: none;
                               }
                       
                               .footer a:hover {
                                   text-decoration: underline;
                               }
                           </style>
                       </head>
                       
                       <body>
                       <div class="email-container">
                           <div class="content">
                               <h2>Reset Your Password</h2>
                               <p style="color: #333333 !important;">We received a request to reset your password for your RushR account. To proceed, please use the following One-Time Password (OTP):</p>
                               <div class="otp" style="color: #ff0000 !important;">[Your OTP Code]</div>
                               <p style="color: #333333 !important;">This OTP is valid for the next 10 minutes. Please enter it in the app to reset your password.</p>
                               <p style="color: #333333 !important;">If you didn’t request a password reset, you can safely ignore this email. Your password will remain unchanged.</p>
                               <p style="color: #333333 !important;">Feel free to reach out to our support team at
                                   <a href="mailto:support@rushr.org" style="color: #ff0000; text-decoration: none;">support@rushr.org</a>
                                   if you have any questions.</p>
                               <p style="color: #333333 !important;">Thank you for using RushR!</p>
                           </div>
                           <div class="footer">
                               <p>Best Regards,<br>RushR Team</p>
                           </div>
                       
                           <img src="[URL]" style="width: 100px; height: auto; margin: 10px auto; display: block;" alt="RushR Logo">
                       </div>
                       </body>
                       
                       </html>
                                
                """;
        return template.replace("[Your OTP Code]",otp).replace("[URL]",imageUrl);
    }


}
