package com.example.avengatwitterweatherapp.constants;

import com.example.avengatwitterweatherapp.model.SortDir;
import com.example.avengatwitterweatherapp.model.SortField;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class RocketStrikeConstants {
    public static final String SORT_BY_REGION = SortField.REGION.toString();
    public static final String ASC_ORDER = SortDir.ASC.toString();
    public static final int MINUTES_DIFFERENCE_BETWEEN_EQUAL_STRIKES = 15;
    public static final List<String> SORT_FIELDS = Arrays.stream(SortField.values()).map(Enum::toString).toList();
    public static final List<String> SORT_DIR = Arrays.stream(SortDir.values()).map(Enum::toString).toList();
    public static final DateTimeFormatter STRIKE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
}
