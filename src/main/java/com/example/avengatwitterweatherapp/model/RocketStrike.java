package com.example.avengatwitterweatherapp.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;

import static com.example.avengatwitterweatherapp.constants.RocketStrikeConstants.MINUTES_DIFFERENCE;

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
    private LocalDateTime strikeDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RocketStrike that = (RocketStrike) o;
        return Objects.equals(id, that.id) && Objects.equals(region, that.region) &&
                ChronoUnit.MINUTES.between(strikeDate, that.strikeDate) <= MINUTES_DIFFERENCE;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, region, strikeDate);
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
