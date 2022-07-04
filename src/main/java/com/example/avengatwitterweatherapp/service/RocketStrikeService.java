package com.example.avengatwitterweatherapp.service;

import com.example.avengatwitterweatherapp.dto.RocketStrikeDto;
import com.example.avengatwitterweatherapp.model.Region;
import com.example.avengatwitterweatherapp.model.RocketStrike;

import java.util.List;

public interface RocketStrikeService {
    List<RocketStrike> getRecentRocketStrikes(String sortField, String sortDir);

    List<RocketStrike> getFilteredRocketStrikes(String sinceDateStr, String untilDateStr, List<Region> regions,
                                                String sortField, String sortDir);

    List<RocketStrike> getFilteredRocketStrikes(RocketStrikeDto rocketStrikeDto);

    RocketStrike getRocketStrikeById(long id);
}
