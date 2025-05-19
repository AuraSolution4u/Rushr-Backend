package com.rushr.Service;

import com.rushr.Dto.ResponseDTO;
import com.rushr.Entity.Notification;
import com.rushr.Entity.Usermaster;
import com.rushr.Repository.NotificationRepository;
import com.rushr.Repository.UsermasterRepository;
import com.rushr.Util.Constants;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.scanner.Constant;

@Service
public class NotificationService {

    private static final Logger log= LogManager.getLogger(NotificationService.class);

    @Autowired
    private UsermasterRepository usermasterRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    public ResponseDTO sendNotification(HttpServletRequest request,String message,Long userId)
    {
        ResponseDTO responseDTO=new ResponseDTO();
        try {

            if(message==null)
            {
                log.warn("Empty Notification Message");
                responseDTO.setMessage("Empty Notification Message");
                responseDTO.setStatusCode(Constants.StatusCodes.noContent);
                return responseDTO;
            }

            Usermaster userDetails=usermasterRepository.findByUserId(userId);

            if(userDetails==null)
            {
                log.warn(Constants.Messages.noUserFound);
                responseDTO.setMessage(Constants.Messages.noUserFound);
                responseDTO.setStatusCode(Constants.StatusCodes.notFound);
                return responseDTO;
            }

            Notification notification=new Notification();
            notification.setMessage(message);
            notification.setUserId(userId);
            notification.setViewed(false);
            notificationRepository.save(notification);

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
