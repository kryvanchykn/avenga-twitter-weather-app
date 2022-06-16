package com.example.avengatwitterweatherapp.repository;

import com.example.avengatwitterweatherapp.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    Region findRegionById(Long id);
}
