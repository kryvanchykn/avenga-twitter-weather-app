package com.example.avengatwitterweatherapp.service.impl;

import com.example.avengatwitterweatherapp.model.Region;
import com.example.avengatwitterweatherapp.model.RegionQuery;
import com.example.avengatwitterweatherapp.model.RocketStrike;
import com.example.avengatwitterweatherapp.repository.RocketStrikeRepository;
import com.example.avengatwitterweatherapp.service.RegionService;
import com.example.avengatwitterweatherapp.service.TwitterService;
import com.example.avengatwitterweatherapp.twitter.TwitterAuth;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.example.avengatwitterweatherapp.constants.RegionConstants.*;
import static com.example.avengatwitterweatherapp.constants.TwitterConstants.*;

@Service
public class TwitterServiceImpl implements TwitterService {
    private static final Logger log = LogManager.getLogger(RocketStrikeServiceImpl.class);
    private final RocketStrikeRepository rocketStrikeRepository;
    private final RegionService regionService;

    public TwitterServiceImpl(RocketStrikeRepository rocketStrikeRepository, RegionService regionService) {
        this.rocketStrikeRepository = rocketStrikeRepository;
        this.regionService = regionService;
    }

    @Override
    public void saveRocketStrikesFromTwitter(List<RegionQuery> regionQueries) {
        try {
            batchRegionQuery(regionQueries).forEach(regionQueriesBatch -> {
                Set<RocketStrike> rocketStrikesFromTwitter = findRocketStrikesInTwitter(regionQueriesBatch);
                rocketStrikeRepository.saveAll(rocketStrikesFromTwitter);
            });
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private Set<RocketStrike> findRocketStrikesInTwitter(List<RegionQuery> regionQueries) {
        Set<RocketStrike> rocketStrikes = new HashSet<>();
        try {
            Twitter authObject = TwitterAuth.authenticate();
            Query query = new Query();
            query.setQuery(joinRegionQueries(regionQueries));
            QueryResult result = authObject.search(query);
            rocketStrikes.addAll(mapRocketStrike(result.getTweets()));
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return rocketStrikes;
    }

    private Set<RocketStrike> mapRocketStrike(List<Status> tweets) {
        return tweets.stream().map(tweet -> {
            RocketStrike rocketStrike = new RocketStrike();
            rocketStrike.setRegion(returnRegionByTweet(tweet));
            Date input = tweet.getCreatedAt();
            Instant instant = input.toInstant();
            ZonedDateTime zdt = instant.atZone(ZoneId.of("Europe/Kiev"));
            LocalDateTime date = zdt.toLocalDateTime();
            rocketStrike.setStrikeDate(date);
            return rocketStrike;
        }).collect(Collectors.toSet());
    }

    private String joinRegionQueries(List<RegionQuery> regionQueries) {
        StringBuilder finalQuery = new StringBuilder();
        String usersQuery = FROM + String.join(OR + FROM, USERS);
        String keywordsQuery = String.join(OR, KEYWORDS);
        regionQueries.forEach(trq -> finalQuery.append(formQueryByRegion(trq)));
        return usersQuery + finalQuery.delete(finalQuery.lastIndexOf(OR), finalQuery.length()) + AND + keywordsQuery;
    }

    private String formQueryByRegion(RegionQuery regionQuery) {
        return " (" + SINCE + regionQuery.getSinceDate() +
                UNTIL + regionQuery.getUntilDate() +
                formatRegionName(regionQuery.getRegion().getRegionName()) +
                OR + formatRegionAltName(regionQuery.getRegion().getRegionAltName()) + ")" + OR;
    }

    private String formatRegionName(String regionName) {
        return " " + regionName.substring(0, regionName.length() - REGION_NAME_SUBS_NUMBER)
                + REGION_NAME_NEW_END;
    }

    private String formatRegionAltName(String regionAltName) {
        return " " + regionAltName.substring(0, regionAltName.length() -
                REGION_ALT_NAME_SUBS_NUMBER) + REGION_ALT_NAME_NEW_END;
    }

    private Region returnRegionByTweet(Status tweet) {
        List<Region> regions = regionService.getAllRegions();
        return regions.stream().filter(region ->
                tweet.getText().contains(formatRegionName(region.getRegionName())) ||
                        tweet.getText().contains(formatRegionAltName(region.getRegionAltName()))).findAny().orElseThrow();
    }

    private Stream<List<RegionQuery>> batchRegionQuery(List<RegionQuery> regionQuery) {
        int size = regionQuery.size();
        if (size <= 0) {
            return Stream.empty();
        }
        int fullChunks = (size - 1) / REGION_QUERY_BATCH_LEN;
        return IntStream.range(0, fullChunks + 1).mapToObj(
                n -> regionQuery.subList(n * REGION_QUERY_BATCH_LEN, n == fullChunks ? size : (n + 1) * REGION_QUERY_BATCH_LEN));
    }
}
