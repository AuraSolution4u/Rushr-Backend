package com.rushr.Controller;

import com.rushr.Dto.ResponseDTO;
import com.rushr.Dto.UserDetailsRequestDTO;
import com.rushr.Service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/restAPI/login")
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping(value = "/user/registration")
    public ResponseDTO registerUser(HttpServletRequest request,@RequestBody UserDetailsRequestDTO userDetailsRequestDTO)
    {
        return loginService.userRegistration(request, userDetailsRequestDTO);
    }

    @PostMapping(value = "/authenticateUser")
    public ResponseDTO authenticateUser(HttpServletRequest request,@RequestBody String jsonData)
    {
        return loginService.loginIn(request,jsonData);
    }
    @PostMapping(value = "/sendOTP")
    public ResponseDTO sendOTP(HttpServletRequest request,@RequestBody String jsonData)
    {
        return loginService.sendOTPToEmail(request,jsonData);
    }

    @PostMapping(value = "/validateOTP")
    public ResponseDTO authenticateOTP(HttpServletRequest request,@RequestBody String jsonData)
    {
        return loginService.validateOTP(request,jsonData);
    }
    @PutMapping(value = "/resetPassword")
    public ResponseDTO resetPassword(HttpServletRequest request,@RequestBody String jsonData)
    {
        return loginService.resetPassword(request,jsonData);
    }

    @PutMapping(value = "/updateProfile")
    public ResponseDTO updateProfile(HttpServletRequest request,@RequestBody UserDetailsRequestDTO userDetailsRequestDTO)
    {
        return loginService.updateProfile(request,userDetailsRequestDTO);
    }

    @PostMapping(value = "/getAllUsers")
    public ResponseDTO getAllUsers(HttpServletRequest request,@RequestBody String jsonData)
    {
        return loginService.getAllUsers(jsonData);
    }

//    public ResponseDTO getUnAssignedUser(HttpServletRequest request,@RequestBody String jsonData)
//    {
//        return loginService.
//    }








}
