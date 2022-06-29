package com.example.avengatwitterweatherapp.serviceTest;

import com.example.avengatwitterweatherapp.model.Region;
import com.example.avengatwitterweatherapp.model.RocketStrike;
import com.example.avengatwitterweatherapp.model.Weather;
import com.example.avengatwitterweatherapp.service.WeatherService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherServiceTest {
    @MockBean
    private WeatherService weatherService;


    @Test
    public void getWeatherTest() {
        Region region1 = new Region(1, "Львівщина", "Львівська область", "Lviv", "Lviv");
        RocketStrike rocketStrike1 = new RocketStrike(1, region1, LocalDateTime.of(2022, 2, 24, 0, 0, 0));
        Weather weather = new Weather(region1, "2022-06-24 00:00", 13.4, 8.6, 88, 12.9, 49);
//        when(weatherService.getWeather(rocketStrike1)).thenReturn(weather);
        assertEquals(weather, weatherService.getWeather(rocketStrike1));
    }
}
