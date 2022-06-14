package com.example.avengatwitterweatherapp.repository;

import com.example.avengatwitterweatherapp.model.RocketStrike;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;


import java.time.LocalDateTime;
import java.util.List;

public interface RocketStrikeRepository extends JpaRepository<RocketStrike, Long> {
    List<RocketStrike> findRocketStrikeByStrikeDateBetween(LocalDateTime sinceDate, LocalDateTime untilDate, Sort sort);
}
