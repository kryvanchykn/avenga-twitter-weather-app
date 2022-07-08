package com.example.avengatwitterweatherapp.service;

import com.example.avengatwitterweatherapp.model.Region;

import java.util.List;

public interface RegionService {
    List<Region> getAllRegions();

    List<Region> getRegionsById(List<Long> ids);
}
