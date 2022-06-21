package com.example.avengatwitterweatherapp.service;

import com.example.avengatwitterweatherapp.model.Region;
import com.example.avengatwitterweatherapp.model.RocketStrike;
import com.example.avengatwitterweatherapp.dto.RocketStrikeDto;

import java.time.LocalDateTime;
import java.util.List;

public interface RocketStrikeService {
    List<RocketStrike> getRecentRocketStrikes(String sortField, String sortDir);

    List<RocketStrike> getFilteredRocketStrikes(String sinceDateStr, String untilDateStr, List<Region> regions,
                                                String sortField, String sortDir);

    List<RocketStrike> getRocketStrikesFromDB(LocalDateTime sinceDate, LocalDateTime untilDate, List<Region> regions,
                                                    String sortField, String sortDirection);
    LocalDateTime getFirstRocketStrikeDateRecord(Region region);
    LocalDateTime getLastRocketStrikeDateRecord(Region region);
    RocketStrike getRocketStrikeById(Long id);

    RocketStrikeDto validateRocketStrikeParamsWrapper(RocketStrikeDto paramsWrapper);
}
