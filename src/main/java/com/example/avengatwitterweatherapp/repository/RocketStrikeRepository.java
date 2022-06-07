package com.example.avengatwitterweatherapp.repository;

import com.example.avengatwitterweatherapp.model.RocketStrike;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface RocketStrikeRepository extends JpaRepository<RocketStrike, Long> {
    Set<RocketStrike> findRocketStrikeByStrikeDateBetween(Date sinceDate, Date untilDate);
}
