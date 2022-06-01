package com.example.avengatwitterweatherapp.service;

import com.example.avengatwitterweatherapp.model.RocketStrike;
import org.springframework.data.domain.Page;

import java.util.HashSet;

public interface RocketStrikeService {
    HashSet<RocketStrike> getAllRocketStrikes();
    Page<RocketStrike> getSortedRocketStrikes(String sortField, String sortDirection);
}
