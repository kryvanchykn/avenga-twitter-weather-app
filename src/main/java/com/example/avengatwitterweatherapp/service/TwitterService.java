package com.example.avengatwitterweatherapp.service;

import com.example.avengatwitterweatherapp.model.RegionQuery;

import java.util.List;

public interface TwitterService {
    void saveRocketStrikesFromTwitter(List<RegionQuery> regionQueries);
}
