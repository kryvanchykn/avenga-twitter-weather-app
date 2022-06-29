package com.example.avengatwitterweatherapp.service.impl;

import com.example.avengatwitterweatherapp.exceptions.WeatherForecastNotFoundException;
import com.example.avengatwitterweatherapp.model.Region;
import com.example.avengatwitterweatherapp.model.RocketStrike;
import com.example.avengatwitterweatherapp.model.Weather;
import com.example.avengatwitterweatherapp.service.WeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.io.IOException;

import static com.example.avengatwitterweatherapp.constants.WeatherConstants.*;

@Service
public class WeatherServiceImpl implements WeatherService {
    private static final Logger log = LogManager.getLogger(WeatherServiceImpl.class);

    @Override
    public Weather getWeather(RocketStrike rocketStrike) {
        int strikeHour =  rocketStrike.getStrikeDate().getHour();

        OkHttpClient client = new OkHttpClient();
        String APIkey = System.getenv("WEATHER_API_KEY");
        Request request = new Request.Builder()
                .url("http://api.weatherapi.com/v1/history.json?key=" + APIkey + "&q=" +
                        rocketStrike.getRegion().getRegionalCentreWeather()+ "&dt="
                        + rocketStrike.getStrikeDate().toLocalDate())
                .build();

        try {

            Response response = client.newCall(request).execute();
            assert response.body() != null;
            log.debug("response body is null");
            return mapWeather(new JSONObject(response.body().string()), strikeHour, rocketStrike.getRegion());
        }catch (IOException | JSONException e){
            log.error(e.getMessage());
        }
        throw new WeatherForecastNotFoundException("Weather forecast for rocket strike with id=" + rocketStrike.getId()
                + " not found, because strike date was more than week ago");
    }


    private Weather mapWeather(JSONObject jsonWeather, int hour, Region region) throws JSONException, JsonProcessingException {
        String hourArray = jsonWeather.getJSONObject(FORECAST).getJSONArray(FORECAST_DAY).getJSONObject(0)
                .getJSONArray(HOUR).toString();
        ObjectMapper mapper = new ObjectMapper();
        Weather[] weatherArray = mapper.readValue(hourArray, Weather[].class);
        weatherArray[hour].setRegion(region);
        return weatherArray[hour];
    }

}
