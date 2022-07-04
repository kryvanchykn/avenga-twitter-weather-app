package com.example.avengatwitterweatherapp.serviceTest;

import com.example.avengatwitterweatherapp.exceptions.WeatherForecastNotFoundException;
import com.example.avengatwitterweatherapp.model.Region;
import com.example.avengatwitterweatherapp.model.RocketStrike;
import com.example.avengatwitterweatherapp.service.impl.WeatherServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherServiceTest {
    @InjectMocks
    private WeatherServiceImpl weatherService;


    @Test
    public void getWeatherThrowBadDateRangeExceptionTest() {
        Region region1 = new Region(1, "Львівщина", "Львівська область", "Lviv", "Lviv");
        RocketStrike rocketStrike1 = new RocketStrike(1, region1, LocalDateTime.now().minusDays(10));

        assertThrows(WeatherForecastNotFoundException.class, () -> weatherService.getWeather(rocketStrike1));
    }

    @Test
    public void getWeatherDoesNotThrowExceptionTest() {
        Region region1 = new Region(1, "Львівщина", "Львівська область", "Lviv", "Lviv");
        RocketStrike rocketStrike1 = new RocketStrike(1, region1, LocalDateTime.now());

        assertDoesNotThrow(() -> weatherService.getWeather(rocketStrike1));
    }
}
