package com.rushr.Util;


import com.rushr.Dto.ResponseDTO;
import com.rushr.Repository.UsermasterRepository;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@Data
public class EmptyResponseUtil {

    private static final Logger log= LogManager.getLogger(EmptyResponseUtil.class);

    public EmptyResponseUtil() {
    }

    @Autowired
    private UsermasterRepository usermasterRepository;

    public ResponseDTO emptyResponse(String message)
    {
        ResponseDTO responseDTO=new ResponseDTO();
        responseDTO.setMessage(message);
        responseDTO.setStatusCode(Constants.StatusCodes.noContent);
        return responseDTO;
    }

    public ResponseDTO emptyUserResponse()
    {
        ResponseDTO responseDTO=new ResponseDTO();
        responseDTO.setMessage(Constants.Messages.noUserFound);
        responseDTO.setStatusCode(Constants.StatusCodes.notFound);

        return responseDTO;
    }


    public ResponseDTO emptyChapterResponse()
    {
        ResponseDTO responseDTO=new ResponseDTO();
        responseDTO.setMessage(Constants.Messages.chapterNotFound);
        responseDTO.setStatusCode(Constants.StatusCodes.noContent);
        return responseDTO;
    }



}
