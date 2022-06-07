package com.example.avengatwitterweatherapp.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

import static com.example.avengatwitterweatherapp.constants.TwitterConstants.MIN_15;

@Entity
@Table(name = "rocket_strikes")
@Getter
@Setter
public class RocketStrike {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @OneToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @Column(name = "strike_date")
    private Date strikeDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RocketStrike that = (RocketStrike) o;
        return Objects.equals(region, that.region) &&
                Math.abs(this.strikeDate.getTime() - that.strikeDate.getTime()) < MIN_15;
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, strikeDate);
    }

    @Override
    public String toString() {
        return "RocketStrike{" +
                "id=" + id +
                ", region=" + region +
                ", strikeDate=" + strikeDate +
                '}';
    }
}
