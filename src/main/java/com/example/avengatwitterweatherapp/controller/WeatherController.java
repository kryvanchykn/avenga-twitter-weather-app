package com.example.avengatwitterweatherapp.controller;

import com.example.avengatwitterweatherapp.exceptions.RocketStrikeNotFoundException;
import com.example.avengatwitterweatherapp.model.RocketStrike;
import com.example.avengatwitterweatherapp.model.Weather;
import com.example.avengatwitterweatherapp.service.RocketStrikeService;
import com.example.avengatwitterweatherapp.service.WeatherService;
import com.example.avengatwitterweatherapp.service.impl.WeatherServiceImpl;
import netscape.javascript.JSObject;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;

import static com.example.avengatwitterweatherapp.constants.WeatherConstants.*;

@RestController
public class WeatherController {
    private static final Logger log = LogManager.getLogger(WeatherController.class);
    private final RocketStrikeService rocketStrikeService;
    private final WeatherService weatherService;

    public WeatherController(RocketStrikeService rocketStrikeService, WeatherService weatherService) {
        this.rocketStrikeService = rocketStrikeService;
        this.weatherService = weatherService;
    }

//    @GetMapping("/forecast/{id}")
//    public String showForecast(@PathVariable Long id, Model model) {
//        RocketStrike rocketStrike = rocketStrikeService.getRocketStrikeById(id);
//        Weather weather = weatherService.getWeather(rocketStrike);
//
//        log.debug(weather);
//        model.addAttribute("rocketStrike", rocketStrike);
//        model.addAttribute("weather", weather);
//        return "show_forecast.html";
//    }

    @GetMapping("/forecast/{id}")
    public Weather showForecast(@PathVariable Long id, HttpServletResponse response) {
        try{
            RocketStrike rocketStrike = rocketStrikeService.getRocketStrikeById(id);
            return weatherService.getWeather(rocketStrike);
        } catch(RocketStrikeNotFoundException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}
