package com.example.avengatwitterweatherapp.controller;

import com.example.avengatwitterweatherapp.model.RocketStrike;
import com.example.avengatwitterweatherapp.model.Weather;
import com.example.avengatwitterweatherapp.service.RocketStrikeService;
import com.example.avengatwitterweatherapp.service.WeatherService;
import netscape.javascript.JSObject;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static com.example.avengatwitterweatherapp.constants.WeatherConstants.*;

@Controller
public class WeatherController {
    private final RocketStrikeService rocketStrikeService;
    private final WeatherService weatherService;

    public WeatherController(RocketStrikeService rocketStrikeService, WeatherService weatherService) {
        this.rocketStrikeService = rocketStrikeService;
        this.weatherService = weatherService;
    }

    @GetMapping("/forecast/{id}")
    public String showForecast(@PathVariable Long id, Model model) throws JSONException {
        RocketStrike rocketStrike = rocketStrikeService.getRocketStrikeById(id);
        Weather weather = weatherService.getWeather(rocketStrike);

        System.out.println(weather);
        model.addAttribute("rocketStrike", rocketStrike);
        model.addAttribute("weather", weather);
        return "show_forecast.html";
    }
}
