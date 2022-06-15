package com.example.avengatwitterweatherapp.utils;

import com.example.avengatwitterweatherapp.model.RocketStrike;
import com.example.avengatwitterweatherapp.service.impl.RocketStrikeServiceImpl;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class RocketStrikeTimeFilter {
    private static final Logger log = LogManager.getLogger(RocketStrikeServiceImpl.class);

    public static List<RocketStrike> filterByTime(LocalDateTime sinceDate, LocalDateTime untilDate,
                                                  List<RocketStrike> rocketStrikes){
        return rocketStrikes.stream().filter(rocketStrike -> {
            boolean b = rocketStrike.getStrikeDate().isAfter(sinceDate) &&
                    rocketStrike.getStrikeDate().isBefore(untilDate);
            log.debug(rocketStrike.getStrikeDate());
            return b;
        }).collect(Collectors.toList());
    }
}
