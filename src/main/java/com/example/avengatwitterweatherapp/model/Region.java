package com.example.avengatwitterweatherapp.model;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "regions")
@Data
public class Region implements Comparable<Region> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "region_name")
    private String regionName;

    @Column(name = "region_alt_name")
    private String regionAltName;

    @Column(name = "regional_centre_weather")
    private String regionalCentreWeather;

    @Column(name = "regional_centre")
    private String regionalCentre;

    @Override
    public int compareTo(@NotNull Region o) {
        return this.getRegionName().compareTo(o.getRegionName());
    }
}
