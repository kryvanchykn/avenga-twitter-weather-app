package com.example.avengatwitterweatherapp.service;

import com.example.avengatwitterweatherapp.model.RocketStrike;

import java.util.HashSet;
import java.util.List;

public interface RocketStrikeService {
    void getRocketStrikesFromTwitter();
    HashSet<RocketStrike> getRocketStrikesFromDB();
    List<RocketStrike> getSortedRocketStrikes(String sortField, String sortDirection);
    RocketStrike getRocketStrikeById(Long id);

}
