package com.example.avengatwitterweatherapp.controllerTest;

import com.example.avengatwitterweatherapp.model.Region;
import com.example.avengatwitterweatherapp.model.RocketStrike;
import com.example.avengatwitterweatherapp.model.Weather;
import com.example.avengatwitterweatherapp.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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


@SpringBootTest
@AutoConfigureMockMvc
public class WeatherControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @Test
    public void showForecastMVCTest() throws Exception {
        Region region1 = new Region(1, "Львівська область", "Львівщина", "Lviv", "Lviv");
        LocalDateTime rocketStrikeDate = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        RocketStrike rocketStrike1 = new RocketStrike(1, region1, rocketStrikeDate);
        Weather weather = new Weather();
        weather.setRegion(region1);
        weather.setTime(rocketStrikeDate.toString());

        when(weatherService.getWeather(rocketStrike1)).thenReturn(weather);

        this.mockMvc.perform(get("/mvc/forecast").param("id", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(containsString(String.valueOf(rocketStrikeDate))))
                .andExpect(content().string(containsString(region1.getRegionalCentre())));
    }

    @Test
    public void showForecastRESTTest() throws Exception {
        Region region1 = new Region(1, "Львівська область", "Львівщина", "Lviv", "Lviv");
        LocalDateTime rocketStrikeDate = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        RocketStrike rocketStrike1 = new RocketStrike(1, region1, rocketStrikeDate);
        Weather weather = new Weather();
        weather.setRegion(region1);
        weather.setTime(rocketStrikeDate.toString());

        when(weatherService.getWeather(rocketStrike1)).thenReturn(weather);

        this.mockMvc.perform(get("/rest/forecast").param("id", String.valueOf(rocketStrike1.getId())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString(String.valueOf(rocketStrikeDate))))
                .andExpect(content().string(containsString(region1.getRegionalCentre())));
    }

    @Test
    public void showForecastRESTThrowsRestExceptionTest() throws Exception {
        long id = new Random().nextLong();
        String errorMessage = "Weather forecast for rocket strike with id=" + id + " is not found";

        this.mockMvc.perform(get("/rest/forecast").param("id", String.valueOf(id)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8)))
                .andExpect(content().string(containsString(errorMessage)));
        ;
    }
}
