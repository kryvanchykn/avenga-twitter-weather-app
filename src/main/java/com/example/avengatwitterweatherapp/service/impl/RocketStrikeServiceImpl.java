package com.example.avengatwitterweatherapp.service.impl;

import com.example.avengatwitterweatherapp.model.Region;
import com.example.avengatwitterweatherapp.model.RocketStrike;

import com.example.avengatwitterweatherapp.repository.RocketStrikeRepository;
import com.example.avengatwitterweatherapp.service.RegionService;
import com.example.avengatwitterweatherapp.service.RocketStrikeService;
import com.example.avengatwitterweatherapp.twitter.TwitterAuth;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.avengatwitterweatherapp.constants.RegionConstants.*;
import static com.example.avengatwitterweatherapp.constants.RegionConstants.REGION_ALT_NAME_NEW_END;
import static com.example.avengatwitterweatherapp.constants.RocketStrikeConstants.SORT_BY_REGION;
import static com.example.avengatwitterweatherapp.constants.TwitterConstants.*;

@Service
public class RocketStrikeServiceImpl implements RocketStrikeService {
    private static final Logger log = LogManager.getLogger(RocketStrikeServiceImpl.class);

    private final RegionService regionService;
    private final RocketStrikeRepository rocketStrikeRepository;


    public RocketStrikeServiceImpl(RegionService regionService, RocketStrikeRepository rocketStrikeRepository) {
        this.regionService = regionService;
        this.rocketStrikeRepository = rocketStrikeRepository;
    }

    @Override
    public void getRocketStrikesFromTwitter(LocalDate sinceDate, LocalDate untilDate) {
        Twitter authObject;
        Set<RocketStrike> rocketStrikes;
        try {
            authObject = TwitterAuth.authenticate();
            rocketStrikes = getRocketStrikes(authObject, regionService.getAllRegions(), sinceDate, untilDate);
            rocketStrikeRepository.saveAll(rocketStrikes);
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }

    @Override
    public Set<RocketStrike> getRocketStrikesFromDB() {
        return new HashSet<>(rocketStrikeRepository.findAll());
    }

    @Override
    public List<RocketStrike> getSortedRocketStrikesFromDB(LocalDateTime sinceDate, LocalDateTime untilDate,
                                                     String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        return rocketStrikeRepository.findRocketStrikeByStrikeDateBetween(sinceDate, untilDate, sort);
    }

    @Override
    public RocketStrike getRocketStrikeById(Long id) {
        return rocketStrikeRepository.findById(id).get();
    }


    @Override
    public LocalDateTime getFirstRocketStrikeDate() {
        RocketStrike rocketStrike = new RocketStrike();
        rocketStrike.setStrikeDate(UNTIL_DATE);
        return rocketStrikeRepository.findAll().stream().min(Comparator.comparing(RocketStrike::getStrikeDate))
                .orElse(rocketStrike).getStrikeDate();
    }

    @Override
    public LocalDateTime getLastRocketStrikeDate() {
        RocketStrike rocketStrike = new RocketStrike();
        rocketStrike.setStrikeDate(SINCE_DATE);
        return rocketStrikeRepository.findAll().stream().max(Comparator.comparing(RocketStrike::getStrikeDate))
                .orElse(rocketStrike).getStrikeDate();
    }


    public Set<RocketStrike> getRocketStrikes(Twitter authObject, List<Region> regions,
                                              LocalDate sinceDate, LocalDate untilDate) {
        Set<RocketStrike> rocketStrikes = new HashSet<>();
        try {
            Query query = new Query();
            QueryResult result;
            for (Region region : regions) {
                query.setQuery(formQueries(region, sinceDate, untilDate));
                result = authObject.search(query);
                rocketStrikes.addAll(mapRocketStrike(result.getTweets(), region));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.debug("ROCKET STRIKES");
        for (RocketStrike rocketStrike:rocketStrikes) {
            log.debug(rocketStrike);
        }
        return rocketStrikes;
    }

    private static Set<RocketStrike> mapRocketStrike(List<Status> tweets, Region region){
        return tweets.stream().map(tweet -> {
            RocketStrike rocketStrike= new RocketStrike();
            rocketStrike.setRegion(region);
            Date input = tweet.getCreatedAt();
            Instant instant = input.toInstant();
            ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
            LocalDateTime date = zdt.toLocalDateTime();
            rocketStrike.setStrikeDate(date);
            log.debug("created at1: " + tweet.getCreatedAt());
            log.debug("created at2: " + date);
            log.debug(rocketStrike);
            return rocketStrike;
        }).collect(Collectors.toSet());
    }

    private static String formQueries(Region region, LocalDate sinceDate, LocalDate untilDate){
        String usersQuery = FROM + String.join(OR + FROM, USERS);
        String keywordsQuery = String.join(OR, KEYWORDS);
        log.debug( usersQuery + SINCE + sinceDate + UNTIL + untilDate + formRegionQuery(region) + AND + keywordsQuery);
        return usersQuery + SINCE + sinceDate + UNTIL + untilDate + formRegionQuery(region) + AND + keywordsQuery;
    }

    private static String formRegionQuery(Region region){
        String formattedRegionName = region.getRegionName().substring(0, region.getRegionName().length() - REGION_NAME_SUBS_NUMBER)
                + REGION_NAME_NEW_END;
        String formattedAltRegionName = region.getRegionAltName().substring(0, region.getRegionAltName().length() -
                REGION_ALT_NAME_SUBS_NUMBER) + REGION_ALT_NAME_NEW_END;
        return " " + formattedRegionName + OR + formattedAltRegionName;
    }
}
