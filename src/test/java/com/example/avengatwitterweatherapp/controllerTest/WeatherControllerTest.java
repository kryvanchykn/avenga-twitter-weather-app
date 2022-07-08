package com.example.avengatwitterweatherapp.controllerTest;

import com.example.avengatwitterweatherapp.controller.WeatherController;
import com.example.avengatwitterweatherapp.exceptions.RocketStrikeNotFoundException;
import com.example.avengatwitterweatherapp.model.Region;
import com.example.avengatwitterweatherapp.model.RocketStrike;
import com.example.avengatwitterweatherapp.model.Weather;
import com.example.avengatwitterweatherapp.service.RocketStrikeService;
import com.example.avengatwitterweatherapp.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(WeatherController.class)
public class WeatherControllerTest {
    private static final Region REGION = new Region(1, "Львівська область", "Львівщина", "Lviv", "Lviv");
    private static final LocalDateTime ROCKET_STRIKE_DATE = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    private static final RocketStrike ROCKET_STRIKE = new RocketStrike(1, REGION, ROCKET_STRIKE_DATE);
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @MockBean
    private RocketStrikeService rocketStrikeService;

    @Test
    public void showForecastMVCTest() throws Exception {
        Weather weather = new Weather();
        weather.setRegion(REGION);
        weather.setTime(ROCKET_STRIKE_DATE.toString());

        when(rocketStrikeService.getRocketStrikeById(ROCKET_STRIKE.getId())).thenReturn(ROCKET_STRIKE);
        when(weatherService.getWeather(ROCKET_STRIKE)).thenReturn(weather);

        this.mockMvc.perform(get("/mvc/forecast").param("id", String.valueOf(REGION.getId())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8)))
                .andExpect(content().string(containsString(String.valueOf(ROCKET_STRIKE_DATE))))
                .andExpect(content().string(containsString(REGION.getRegionalCentre())));
    }

    @Test
    public void showForecastRESTTest() throws Exception {
        Weather weather = new Weather();
        weather.setRegion(REGION);
        weather.setTime(ROCKET_STRIKE_DATE.toString());

        when(rocketStrikeService.getRocketStrikeById(ROCKET_STRIKE.getId())).thenReturn(ROCKET_STRIKE);
        when(weatherService.getWeather(ROCKET_STRIKE)).thenReturn(weather);

        this.mockMvc.perform(get("/rest/forecast").param("id", String.valueOf(ROCKET_STRIKE.getId())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString(String.valueOf(ROCKET_STRIKE_DATE))))
                .andExpect(content().string(containsString(REGION.getRegionalCentre())));
    }

    @Test
    public void showForecastRESTThrowsRestExceptionTest() throws Exception {
        long id = new Random().nextLong();
        String errorMessage = "Weather forecast for rocket strike with id=" + id + " is not found";
        when(rocketStrikeService.getRocketStrikeById(id))
                .thenThrow(new RocketStrikeNotFoundException("Weather forecast for rocket strike with id=" + id + " is not found"));

        this.mockMvc.perform(get("/rest/forecast").param("id", String.valueOf(id)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8)))
                .andExpect(content().string(containsString(errorMessage)));
    }
}
