package com.example.avengatwitterweatherapp.service;

import com.example.avengatwitterweatherapp.model.Region;

import java.time.LocalDate;

public interface TwitterService {
    void saveRocketStrikesFromTwitter(LocalDate sinceDate, LocalDate untilDate, Region region);
}
