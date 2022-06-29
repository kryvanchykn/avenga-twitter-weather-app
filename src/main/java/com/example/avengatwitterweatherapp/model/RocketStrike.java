package com.example.avengatwitterweatherapp.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static com.example.avengatwitterweatherapp.constants.RocketStrikeConstants.MINUTES_DIFFERENCE_BETWEEN_EQUAL_STRIKES;

@Entity
@Table(name = "rocket_strikes", uniqueConstraints = {
        @UniqueConstraint(name = "UniqueRegionAndDate", columnNames = {"region_id", "strike_date"})})
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RocketStrike {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
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
        return Objects.equals(region, that.region) &&
                ChronoUnit.MINUTES.between(strikeDate, that.strikeDate) <= MINUTES_DIFFERENCE_BETWEEN_EQUAL_STRIKES;
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, strikeDate.truncatedTo(ChronoUnit.HOURS));
    }
}
