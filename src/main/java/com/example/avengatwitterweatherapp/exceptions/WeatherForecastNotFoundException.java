package com.example.avengatwitterweatherapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class WeatherForecastNotFoundException extends RuntimeException {
    public WeatherForecastNotFoundException(String message) {
        super(message);
    }
}
