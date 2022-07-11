package com.example.avengatwitterweatherapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class RegionQuery {
    private Region region;
    private LocalDate sinceDate;
    private LocalDate untilDate;
}
