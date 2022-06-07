package com.example.avengatwitterweatherapp.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "regions")
@Getter
@Setter
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "region_name")
    private String regionName;

    @Column(name = "region_alt_name")
    private String regionAltName;

    @Column(name = "regional_centre_ua")
    private String regionalCentreUa;

    @Column(name = "regional_centre_en")
    private String regionalCentreEn;


    @Override
    public String toString() {
        return "Region{" +
                "id=" + id +
                ", regionName='" + regionName + '\'' +
                ", regionAltName='" + regionAltName + '\'' +
                ", regionalCentreUa='" + regionalCentreUa + '\'' +
                ", regionalCentreEn='" + regionalCentreEn + '\'' +
                '}';
    }
}
