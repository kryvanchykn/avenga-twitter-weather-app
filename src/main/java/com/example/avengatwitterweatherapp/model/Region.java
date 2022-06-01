package com.example.avengatwitterweatherapp.model;

import javax.persistence.*;

@Entity
@Table(name = "regions")
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionAltName() {
        return regionAltName;
    }

    public void setRegionAltName(String regionAltName) {
        this.regionAltName = regionAltName;
    }

    public String getRegionalCentreUa() {
        return regionalCentreUa;
    }

    public void setRegionalCentreUa(String regionalCentreUa) {
        this.regionalCentreUa = regionalCentreUa;
    }

    public String getRegionalCentreEn() {
        return regionalCentreEn;
    }

    public void setRegionalCentreEn(String regionalCentreEn) {
        this.regionalCentreEn = regionalCentreEn;
    }

}
