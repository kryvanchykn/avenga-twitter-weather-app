package com.example.avengatwitterweatherapp.controller;


import com.example.avengatwitterweatherapp.service.impl.WeatherServiceImpl;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {
    private static final Logger log = LogManager.getLogger(CustomErrorController.class);


    @RequestMapping("/mvc/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            log.debug("statusCode=" + statusCode);
            if(statusCode == HttpStatus.BAD_REQUEST.value()) {
                return "errors/error-400";
            }
            else if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "errors/error-404";
            }
            else if(statusCode == HttpStatus.NOT_ACCEPTABLE.value()) {
                return "errors/error-406";
            }
        }
        return "error";
    }
}