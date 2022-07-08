package com.example.avengatwitterweatherapp.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class CustomErrorController implements ErrorController {
    static final int BAD_REQUEST_STATUS = 400;
    static final int NOT_FOUND_STATUS = 404;
    static final int NOT_ACCEPTABLE_STATUS = 406;

    @RequestMapping("/mvc/error")
    public String handleError(HttpServletRequest request, HttpServletResponse response) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            switch (statusCode) {
                case (BAD_REQUEST_STATUS) -> {
                    response.setStatus(BAD_REQUEST_STATUS);
                    return "errors/error-400";
                }
                case (NOT_FOUND_STATUS) -> {
                    response.setStatus(NOT_FOUND_STATUS);
                    return "errors/error-404";
                }
                case (NOT_ACCEPTABLE_STATUS) -> {
                    response.setStatus(NOT_ACCEPTABLE_STATUS);
                    return "errors/error-406";
                }
            }
        }
        response.setStatus(BAD_REQUEST_STATUS);
        return "errors/error";
    }
}