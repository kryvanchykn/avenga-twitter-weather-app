package com.example.avengatwitterweatherapp.service;

import com.example.avengatwitterweatherapp.model.RocketStrike;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface RocketStrikeService {
    void saveRocketStrikesFromTwitter(LocalDate sinceDate, LocalDate untilDate);

    List<RocketStrike> getSortedRocketStrikesFromDB(LocalDateTime sinceDate, LocalDateTime untilDate,
                                                    String sortField, String sortDirection);
    LocalDateTime getFirstRocketStrikeDate();
    LocalDateTime getLastRocketStrikeDate();
    RocketStrike getRocketStrikeById(Long id);
}
