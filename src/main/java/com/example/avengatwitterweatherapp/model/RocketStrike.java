package com.example.avengatwitterweatherapp.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "rocket_strikes")
public class RocketStrike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Region region;

    @Column(name = "strike_date")
    private Date strikeDate;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }

    @OneToOne
    @JoinColumn(name = "region_id")
    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Date getStrikeDate() {
        return strikeDate;
    }

    public void setStrikeDate(Date strikeDate) {
        this.strikeDate = strikeDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RocketStrike that = (RocketStrike) o;
        return Objects.equals(id, that.id) && Objects.equals(region, that.region) &&
                Math.abs(this.strikeDate.getTime() - that.strikeDate.getTime()) < 900000;
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, strikeDate);
    }
}
