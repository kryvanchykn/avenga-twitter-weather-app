package com.example.avengatwitterweatherapp.service;

import com.example.avengatwitterweatherapp.model.RocketStrike;

import java.util.HashSet;
import java.util.List;

public interface RocketStrikeService {
    HashSet<RocketStrike> getRocketStrikesFromTwitter();
    HashSet<RocketStrike> getRocketStrikesFromDB();
    List<RocketStrike> getSortedRocketStrikes(String sortField, String sortDirection);

}
