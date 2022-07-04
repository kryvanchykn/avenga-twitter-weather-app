package com.example.avengatwitterweatherapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NoSelectedRegionsException extends RuntimeException {
    public NoSelectedRegionsException(String message) {
        super(message);
    }
}
