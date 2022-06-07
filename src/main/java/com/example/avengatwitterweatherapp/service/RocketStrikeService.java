package com.example.avengatwitterweatherapp.service;

import com.example.avengatwitterweatherapp.model.RocketStrike;

import java.util.HashSet;

public interface RocketStrikeService {
    HashSet<RocketStrike> getRocketStrikesFromTwitter();
    HashSet<RocketStrike> getRocketStrikesFromDB();
    HashSet<RocketStrike> getSortedRocketStrikes(String sortField, String sortDirection);

}
