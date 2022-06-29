package com.example.avengatwitterweatherapp.service;

import com.example.avengatwitterweatherapp.dto.RocketStrikeDto;
import com.example.avengatwitterweatherapp.model.Region;
import com.example.avengatwitterweatherapp.model.RocketStrike;

import java.time.LocalDateTime;
import java.util.List;

public interface RocketStrikeService {
    List<RocketStrike> getRecentRocketStrikes(String sortField, String sortDir);

    List<RocketStrike> getFilteredRocketStrikes(String sinceDateStr, String untilDateStr, List<Region> regions,
                                                String sortField, String sortDir);

    List<RocketStrike> getFilteredRocketStrikes(RocketStrikeDto rocketStrikeDto);

    List<RocketStrike> getRocketStrikesFromDB(LocalDateTime sinceDate, LocalDateTime untilDate, List<Region> regions,
                                                    String sortField, String sortDirection);
    LocalDateTime getFirstRocketStrikeDateRecord(Region region);
    LocalDateTime getLastRocketStrikeDateRecord(Region region);
    RocketStrike getRocketStrikeById(long id);
}
