package com.example.avengatwitterweatherapp.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather {

    @JsonProperty("time")
    private String time;

    @JsonProperty("temp_c")
    private String temp;

    @JsonProperty("wind_kph")
    private double windSpeed;

    @JsonProperty("humidity")
    private int humidity;

    @JsonProperty("feelslike_c")
    private double feelsLike;

    @JsonProperty("cloud")
    private int cloud;

    @Override
    public String toString() {
        return "Weather{" +
                "time='" + time + '\'' +
                ", temp='" + temp + '\'' +
                ", windKPH=" + windSpeed +
                ", humidity=" + humidity +
                ", feelslike=" + feelsLike +
                ", cloud=" + cloud +
                '}';
    }
}

