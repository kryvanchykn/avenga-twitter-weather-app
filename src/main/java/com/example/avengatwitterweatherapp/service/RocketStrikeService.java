package com.example.avengatwitterweatherapp.service;

import com.example.avengatwitterweatherapp.model.RocketStrike;
import org.springframework.data.domain.Page;

import java.util.HashSet;
import java.util.List;

public interface RocketStrikeService {
    HashSet<RocketStrike> getAllRocketStrikes();
    List<RocketStrike> getSortedRocketStrikes(String sortField, String sortDirection);

//    RocketStrike saveRocketStrike(RocketStrike rocketStrike);
}
