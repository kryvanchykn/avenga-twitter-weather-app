package com.example.avengatwitterweatherapp.service;

import com.example.avengatwitterweatherapp.model.Region;

import java.time.LocalDate;
import java.util.List;

public interface TwitterService {
    void saveRocketStrikesFromTwitter(LocalDate sinceDate, LocalDate untilDate, Region region);
}
