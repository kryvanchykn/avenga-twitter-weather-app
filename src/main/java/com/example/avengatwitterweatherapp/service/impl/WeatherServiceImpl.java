package com.example.avengatwitterweatherapp.service.impl;

import com.example.avengatwitterweatherapp.model.RocketStrike;
import com.example.avengatwitterweatherapp.model.Weather;
import com.example.avengatwitterweatherapp.service.WeatherService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.example.avengatwitterweatherapp.constants.WeatherConstants.*;

@Service
public class WeatherServiceImpl implements WeatherService {
    private static final Logger log = LogManager.getLogger(WeatherServiceImpl.class);

    @Override
    public Weather getWeather(RocketStrike rocketStrike) {
        OkHttpClient client = new OkHttpClient();  //using OKHTTP dependency . You have to add this mannually form OKHTTP website
        String APIkey = System.getenv("WEATHER_API_KEY");
        Request request = new Request.Builder()
                .url("http://api.openweathermap.org/data/2.5/weather?q="+ rocketStrike.getRegion().getRegionalCentreEn()
                        + "&appid="+ APIkey)
                .build();

        try {
            Response response = client.newCall(request).execute();
            assert response.body() != null;
            return mapWeather(new JSONObject(response.body().string()));
        }catch (IOException | JSONException e){
            log.debug(e.getMessage());
        }
        return null;
    }

    private static Weather mapWeather(JSONObject jsonWeather) throws JSONException {
        System.out.println(jsonWeather);
        Weather weather = new Weather();

        JSONObject weatherMain = jsonWeather.getJSONObject(MAIN);
        JSONArray weatherArray = jsonWeather.getJSONArray(WEATHER);

        String description =  weatherArray.getJSONObject(0).getString(DESCRIPTION);
        double feelsLike = weatherMain.getDouble(FEELS_LIKE);
        double temp = weatherMain.getDouble(TEMP);
        double minTemp = weatherMain.getDouble(MIN_TEMP);
        double maxTemp = weatherMain.getDouble(MAX_TEMP);
        double pressure = weatherMain.getDouble(PRESSURE);
        double humidity = weatherMain.getDouble(HUMIDITY);
        double wind = jsonWeather.getJSONObject(WIND).getDouble(SPEED);

        weather.setDescription(description);
        weather.setFeelsLike(feelsLike);
        weather.setTemp(temp);
        weather.setMinTemp(minTemp);
        weather.setMaxTemp(maxTemp);
        weather.setPressure(pressure);
        weather.setHumidity(humidity);
        weather.setWind(wind);

        return weather;
    }
}
