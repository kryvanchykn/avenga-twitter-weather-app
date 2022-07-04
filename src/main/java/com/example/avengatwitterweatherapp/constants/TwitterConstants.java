package com.example.avengatwitterweatherapp.constants;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TwitterConstants {
    public static final Set<String> USERS = Stream.of("5channel", "nakipeloua", "ICTV_Fakty", "EuropeanPravda", "tsnua")
            .collect(Collectors.toSet());
    public static final Set<String> KEYWORDS = Stream.of("ракетний", "удар", "вибухи", "ракета", "вибух")
            .collect(Collectors.toSet());
    public static final LocalDateTime BOUNDARY_DATE = LocalDateTime.of(2022, 6, 28, 0, 0, 0);
    public static final String OR = " OR ";
    public static final String AND = " AND ";
    public static final String FROM = " from:";
    public static final String SINCE = " since:";
    public static final String UNTIL = " until:";

}
