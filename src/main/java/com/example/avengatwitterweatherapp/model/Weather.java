package com.example.avengatwitterweatherapp.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class Weather {
    private Region region;

    @JsonProperty("time")
    private String time;

    @JsonProperty("temp_c")
    private double temp;

    @JsonProperty("wind_kph")
    private double windSpeed;

    @JsonProperty("humidity")
    private int humidity;

    @JsonProperty("feelslike_c")
    private double feelsLike;

    @JsonProperty("cloud")
    private int cloud;
}

