package com.example.avengatwitterweatherapp.repository;

import com.example.avengatwitterweatherapp.model.RocketStrike;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

public interface RocketStrikeRepositoryCustom {
    HashSet<RocketStrike> findSortedRocketStrikeByStrikeDateBetween(String sinceDate, String untilDate);
}
