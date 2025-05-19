package com.rushr.Controller;

import com.rushr.Dto.ResponseDTO;
import com.rushr.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/restAPI/user")
@CrossOrigin(origins = "*",allowedHeaders = "*")

public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/sendBid")
    public ResponseDTO sendBidToUser(HttpServletRequest request, @RequestBody String jsonData)
    {
        return userService.sendBidToUser(request,jsonData);
    }

    public ResponseDTO acceptBid(HttpServletRequest request,@RequestBody String jsonData)
    {
        return userService.acceptBidRequest(request,jsonData);
    }
    @PostMapping(value = "/requestToJoinAsMember")
    public ResponseDTO requestToJoinAsMember(HttpServletRequest request, @RequestBody String jsonData)
    {
        return userService.requestToJoinAsMember(request,jsonData);
    }

}
