package com.example.avengatwitterweatherapp.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<String> handleInvalidArgument(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrorMap = new HashMap<>();
        List<String> otherErrorsList = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> fieldErrorMap.put(error.getField(), error.getDefaultMessage()));
        ex.getBindingResult().getAllErrors().forEach(error -> {
            if(!fieldErrorMap.containsValue(error.getDefaultMessage())){
                otherErrorsList.add(error.getDefaultMessage());
            }
        });
        fieldErrorMap.forEach((key, value) -> otherErrorsList.add(key + ": " + value));

        return otherErrorsList;
    }

}