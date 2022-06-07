package com.example.avengatwitterweatherapp.constants;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TwitterConstants {
    public static final Set<String> USERS = Stream.of("PushkarPavlo", "nakipeloua", "ICTV_Fakty")
            .collect(Collectors.toSet());
    public static final Set<String> KEYWORDS = Stream.of("ракетний", "удар", "вибухи", "ракета")
            .collect(Collectors.toSet());
    public static final LocalDate SINCE_DATE = LocalDate.of(2022, 2, 24);
    public static final LocalDate UNTIL_DATE = LocalDate.now().plusDays(1);
    public static final String OR = " OR ";
    public static final String AND = " AND ";
    public static final String FROM = " from:";
    public static final Long MIN_15 = 900000L;

}
