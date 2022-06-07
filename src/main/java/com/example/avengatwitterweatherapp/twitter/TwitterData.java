package com.example.avengatwitterweatherapp.twitter;

import com.example.avengatwitterweatherapp.model.Region;
import com.example.avengatwitterweatherapp.model.RocketStrike;
import com.example.avengatwitterweatherapp.repository.RocketStrikeRepository;
import com.example.avengatwitterweatherapp.service.RocketStrikeService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.avengatwitterweatherapp.constants.RegionConstants.*;
import static com.example.avengatwitterweatherapp.constants.TwitterConstants.*;

public class TwitterData {
    private static final Logger log = LogManager.getLogger(TwitterData.class);
    private final RocketStrikeService rocketStrikeService;

    public TwitterData(RocketStrikeService rocketStrikeService) {
        this.rocketStrikeService = rocketStrikeService;
    }




}
