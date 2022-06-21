package com.example.avengatwitterweatherapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RocketStrikeNotFoundException extends RuntimeException {
    public RocketStrikeNotFoundException() {
        super();
    }
    public RocketStrikeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public RocketStrikeNotFoundException(String message) {
        super(message);
    }
    public RocketStrikeNotFoundException(Throwable cause) {
        super(cause);
    }
}
