package com.example.avengatwitterweatherapp.service;

import com.example.avengatwitterweatherapp.model.RocketStrike;
import com.example.avengatwitterweatherapp.model.Weather;

public interface WeatherService {
    Weather getWeather(RocketStrike rocketStrike);
}
