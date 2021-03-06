package com.example.avengatwitterweatherapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RocketStrikeNotFoundException extends RuntimeException {
    public RocketStrikeNotFoundException(String message) {
        super(message);
    }
}
