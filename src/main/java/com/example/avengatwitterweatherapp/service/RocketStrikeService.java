package com.example.avengatwitterweatherapp.service;

import com.example.avengatwitterweatherapp.model.Region;
import com.example.avengatwitterweatherapp.model.RocketStrike;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface RocketStrikeService {
    List<RocketStrike> getRecentRocketStrikes(String sortField, String sortDir);

    List<RocketStrike> getFilteredRocketStrikes(String sinceDateStr, String untilDateStr, List<Region> regions,
                                                String sortField, String sortDir);

    void saveRocketStrikesFromTwitter(LocalDate sinceDate, LocalDate untilDate, List<Region> regions);

    List<RocketStrike> getRocketStrikesFromDB(LocalDateTime sinceDate, LocalDateTime untilDate, List<Region> regions,
                                                    String sortField, String sortDirection);
    LocalDateTime getFirstRocketStrikeDate();
    LocalDateTime getLastRocketStrikeDate();
    RocketStrike getRocketStrikeById(Long id);
}
