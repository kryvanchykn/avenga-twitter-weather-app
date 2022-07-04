package com.example.avengatwitterweatherapp.controller;

import com.example.avengatwitterweatherapp.exceptions.RestException;
import com.example.avengatwitterweatherapp.exceptions.RocketStrikeNotFoundException;
import com.example.avengatwitterweatherapp.exceptions.WeatherForecastNotFoundException;
import com.example.avengatwitterweatherapp.model.RocketStrike;
import com.example.avengatwitterweatherapp.model.Weather;
import com.example.avengatwitterweatherapp.service.RocketStrikeService;
import com.example.avengatwitterweatherapp.service.WeatherService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WeatherController {
    private static final Logger log = LogManager.getLogger(WeatherController.class);

    private final RocketStrikeService rocketStrikeService;
    private final WeatherService weatherService;

    public WeatherController(RocketStrikeService rocketStrikeService, WeatherService weatherService) {
        this.rocketStrikeService = rocketStrikeService;
        this.weatherService = weatherService;
    }

    @GetMapping("/mvc/forecast")
    public String showForecast(@RequestParam Long id, Model model) {
        RocketStrike rocketStrike = rocketStrikeService.getRocketStrikeById(id);
        Weather weather = weatherService.getWeather(rocketStrike);

        model.addAttribute("rocketStrike", rocketStrike);
        model.addAttribute("weather", weather);
        return "show_forecast.html";
    }

    @GetMapping("/rest/forecast")
    @ResponseBody
    public Weather showForecast(@RequestParam Long id) {
        try{
            RocketStrike rocketStrike = rocketStrikeService.getRocketStrikeById(id);
            return weatherService.getWeather(rocketStrike);
        } catch(RocketStrikeNotFoundException | WeatherForecastNotFoundException ex) {
            log.error(ex.getMessage());
            throw new RestException(ex.getMessage());
        }
    }
}
