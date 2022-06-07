package com.example.avengatwitterweatherapp.service;

import com.example.avengatwitterweatherapp.model.RocketStrike;
import com.example.avengatwitterweatherapp.model.Weather;
import org.springframework.boot.configurationprocessor.json.JSONObject;

public interface WeatherService {
    Weather getWeather(RocketStrike rocketStrike);
}
