package com.example.avengatwitterweatherapp.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RocketStrikeParamsWrapper {
    String sinceDate;
    String untilDate;
    List<Long> checkedRegionsId;
    String sortField;
    String sortDir;
}
