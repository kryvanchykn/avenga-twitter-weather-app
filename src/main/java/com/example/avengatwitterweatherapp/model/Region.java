package com.example.avengatwitterweatherapp.model;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "regions")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
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
        return this.getRegionalCentre().compareTo(o.getRegionalCentre());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Region region = (Region) o;
        return Objects.equals(regionName, region.regionName) && Objects.equals(regionAltName, region.regionAltName)
                && Objects.equals(regionalCentreWeather, region.regionalCentreWeather)
                && Objects.equals(regionalCentre, region.regionalCentre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regionName, regionAltName, regionalCentreWeather, regionalCentre);
    }
}
