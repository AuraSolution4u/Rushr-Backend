package com.rushr.Controller;

import com.rushr.Dto.ResponseDTO;
import com.rushr.Service.DashboardService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value ="/restAPI/dashboard")
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping(value = "/getMasterList")
    public ResponseDTO getMasterList(HttpServletRequest request)
    {
        return dashboardService.getMasterData(request);
    }

}
