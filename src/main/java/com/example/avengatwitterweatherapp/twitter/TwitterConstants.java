package com.example.avengatwitterweatherapp.twitter;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TwitterConstants {
    public static final Set<String> USERS = Stream.of("PushkarPavlo", "nakipeloua").collect(Collectors.toSet());
    public static final Set<String> KEYWORDS = Stream.of("ракетний", "удар").collect(Collectors.toSet());
    public static final LocalDate SINCE_DATE = LocalDate.of(2022, 2, 24);
    public static final LocalDate UNTIL_DATE = LocalDate.now().plusDays(1);
}
