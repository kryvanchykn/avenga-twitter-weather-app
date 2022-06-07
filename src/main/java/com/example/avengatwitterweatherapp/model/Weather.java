package com.example.avengatwitterweatherapp.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
public class Weather {
    private String description;
    private double temp;

    @Column(name="min_temp")
    private double minTemp;

    @Column(name="max_temp")
    private double maxTemp;

    private double pressure;

    private double humidity;

    private double wind;

    @Column(name="feels_like")
    private double feelsLike;

    @Override
    public String toString() {
        return "Weather{" +
                "description='" + description + '\'' +
                ", temp=" + temp +
                ", minTemp=" + minTemp +
                ", maxTemp=" + maxTemp +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", wind=" + wind +
                ", feelsLike=" + feelsLike +
                '}';
    }
}
