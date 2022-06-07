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

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.avengatwitterweatherapp.constants.RegionConstants.*;
import static com.example.avengatwitterweatherapp.constants.RegionConstants.REGION_ALT_NAME_NEW_END;
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
    public HashSet<RocketStrike> getRocketStrikesFromTwitter() {
        Twitter authObject;
        HashSet<RocketStrike> rocketStrikes = new HashSet<>();
        try {
            authObject = TwitterAuth.authenticate();
            rocketStrikes = getRocketStrikes(authObject, regionService.getAllRegions(), SINCE_DATE, UNTIL_DATE);
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        rocketStrikeRepository.saveAll(rocketStrikes);
        System.out.println("rocketStrikeRepository.size = " + rocketStrikeRepository.findAll().size());
        return rocketStrikes;
    }

    @Override
    public HashSet<RocketStrike> getRocketStrikesFromDB() {
        return new HashSet<>(rocketStrikeRepository.findAll());
    }

    @Override
    public HashSet<RocketStrike> getSortedRocketStrikes(String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        System.out.println("rocketStrikeRepository.findAll(sort) = " + rocketStrikeRepository.findAll(sort).size());
        return new HashSet<>(rocketStrikeRepository.findAll(sort));
    }


    public HashSet<RocketStrike> getRocketStrikes(Twitter authObject, List<Region> regions,
                                                  LocalDate sinceDate, LocalDate untilDate) {
        HashSet<RocketStrike> rocketStrikes = getRocketStrikesFromDB();
        try {
            Query query = new Query();
            QueryResult result;
            query.setSince(SINCE_DATE.toString());
            query.setUntil(UNTIL_DATE.toString());
            for (Region region : regions) {
                query.setQuery(formQueries(region));
                result = authObject.search(query);
                rocketStrikes.addAll(mapRocketStrike(result.getTweets(), region));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        System.out.println("ROCKET STRIKES");
        for (RocketStrike rocketStrike:rocketStrikes) {
            System.out.println(rocketStrike);

        }
        return rocketStrikes;
    }

    private static Set<RocketStrike> mapRocketStrike(List<Status> tweets, Region region){
        return tweets.stream().map(tweet -> {
            RocketStrike rocketStrike= new RocketStrike();
            rocketStrike.setRegion(region);
            rocketStrike.setStrikeDate(tweet.getCreatedAt());
            System.out.println(rocketStrike);
            return rocketStrike;
        }).collect(Collectors.toSet());
    }

    private static String formQueries(Region region){
        String usersQuery = FROM + String.join(OR + FROM, USERS);
        String keywordsQuery = String.join(OR, KEYWORDS);
        System.out.println(usersQuery + AND + formRegionQuery(region) + AND + keywordsQuery);
        return usersQuery + AND + formRegionQuery(region) + AND + keywordsQuery;
    }

    private static String formRegionQuery(Region region){
        String formattedRegionName = region.getRegionName().substring(0, region.getRegionName().length() - REGION_NAME_SUBS_NUMBER)
                + REGION_NAME_NEW_END;
        String formattedAltRegionName = region.getRegionAltName().substring(0, region.getRegionAltName().length() -
                REGION_ALT_NAME_SUBS_NUMBER) + REGION_ALT_NAME_NEW_END;
        return formattedRegionName + OR + formattedAltRegionName;
    }
}
