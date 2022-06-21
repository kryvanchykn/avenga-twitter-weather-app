package com.example.avengatwitterweatherapp.service.impl;

import com.example.avengatwitterweatherapp.model.Region;
import com.example.avengatwitterweatherapp.repository.RegionRepository;
import com.example.avengatwitterweatherapp.service.RegionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionServiceImpl implements RegionService {
    private final RegionRepository regionRepository;

    public RegionServiceImpl(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    @Override
    public List<Region> getAllRegions() {
        return regionRepository.findAll();
    }

    @Override
    public List<Region> getRegionsById(List<Long> ids) {
        return ids.stream().map(regionRepository::findRegionById).toList();
    }


}
