package com.example.avengatwitterweatherapp.advice;

import com.example.avengatwitterweatherapp.exceptions.RestException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;
import java.util.*;

@RestControllerAdvice
public class ApplicationExceptionHandler {
    private static final Logger log = LogManager.getLogger(ApplicationExceptionHandler.class);
    private static final int BAD_REQUEST_STATUS_CODE = HttpStatus.BAD_REQUEST.value();

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleInvalidArgument(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrorMap = new HashMap<>();
        List<String> otherErrorsList = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> fieldErrorMap.put(error.getField(), error.getDefaultMessage()));
        ex.getBindingResult().getAllErrors().forEach(error -> {
            if (!fieldErrorMap.containsValue(error.getDefaultMessage())) {
                otherErrorsList.add(error.getDefaultMessage());
            }
        });
        fieldErrorMap.forEach((key, value) -> otherErrorsList.add(key + ": " + value));
        log.error("MethodArgumentNotValidException: " + Arrays.toString(otherErrorsList.toArray()));
        return new Error(BAD_REQUEST_STATUS_CODE, "MethodArgumentNotValidException", otherErrorsList).toString();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RestException.class)
    public String handleInvalidRocketStrike(RestException ex) {
        log.error("RestException: " + ex.getMessage());
        return new Error(BAD_REQUEST_STATUS_CODE, "RestException", List.of(ex.getMessage())).toString();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DateTimeParseException.class)
    public String handleInvalidRocketStrike(DateTimeParseException ex) {
        log.error("DateTimeParseException: " + ex.getMessage());
        return new Error(BAD_REQUEST_STATUS_CODE, "DateTimeParseException",
                List.of("Date should match 'yyyy-MM-dd'T'HH:mm' format")).toString();
    }

}