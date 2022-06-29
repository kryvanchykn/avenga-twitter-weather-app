package com.example.avengatwitterweatherapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class BadDateRangeException extends RuntimeException {
    public BadDateRangeException(String message) {
        super(message);
    }
}
