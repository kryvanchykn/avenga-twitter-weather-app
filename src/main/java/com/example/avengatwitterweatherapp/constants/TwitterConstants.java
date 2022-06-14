package com.example.avengatwitterweatherapp.constants;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TwitterConstants {
    public static final Set<String> USERS = Stream.of("PushkarPavlo", "nakipeloua", "ICTV_Fakty")
            .collect(Collectors.toSet());
    public static final Set<String> KEYWORDS = Stream.of("ракетний", "удар", "вибухи", "ракета")
            .collect(Collectors.toSet());
    public static final LocalDateTime SINCE_DATE = LocalDateTime.of(2022, 2, 24, 0, 0, 0);
    public static final LocalDateTime UNTIL_DATE = LocalDateTime.now().plusDays(1);
    public static final String OR = " OR ";
    public static final String AND = " AND ";
    public static final String FROM = " from:";
    public static final String SINCE = " since:";
    public static final String UNTIL = " until:";

}
