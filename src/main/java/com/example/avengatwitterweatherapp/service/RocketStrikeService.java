package com.example.avengatwitterweatherapp.service;

import com.example.avengatwitterweatherapp.model.RocketStrike;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface RocketStrikeService {
    void getRocketStrikesFromTwitter(LocalDate sinceDate, LocalDate untilDate);
    Set<RocketStrike> getRocketStrikesFromDB();
    List<RocketStrike> getSortedRocketStrikesFromDB(LocalDateTime sinceDate, LocalDateTime untilDate,
                                                    String sortField, String sortDirection);
    RocketStrike getRocketStrikeById(Long id);

    LocalDateTime getFirstRocketStrikeDate();
    LocalDateTime getLastRocketStrikeDate();

}
