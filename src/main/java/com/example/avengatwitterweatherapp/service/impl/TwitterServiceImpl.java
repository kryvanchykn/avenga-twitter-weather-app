package com.example.avengatwitterweatherapp.service.impl;

import com.example.avengatwitterweatherapp.model.Region;
import com.example.avengatwitterweatherapp.model.RocketStrike;
import com.example.avengatwitterweatherapp.repository.RocketStrikeRepository;
import com.example.avengatwitterweatherapp.service.TwitterService;
import com.example.avengatwitterweatherapp.twitter.TwitterAuth;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;

import java.time.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.avengatwitterweatherapp.constants.RegionConstants.*;
import static com.example.avengatwitterweatherapp.constants.RegionConstants.REGION_ALT_NAME_NEW_END;
import static com.example.avengatwitterweatherapp.constants.TwitterConstants.*;
import static com.example.avengatwitterweatherapp.constants.TwitterConstants.OR;

@Service
public class TwitterServiceImpl implements TwitterService {
    private static final Logger log = LogManager.getLogger(RocketStrikeServiceImpl.class);
    private final RocketStrikeRepository rocketStrikeRepository;

    public TwitterServiceImpl(RocketStrikeRepository rocketStrikeRepository) {
        this.rocketStrikeRepository = rocketStrikeRepository;
    }

    @Override
    public void saveRocketStrikesFromTwitter(LocalDate sinceDate, LocalDate untilDate, Region region) {
        Set<RocketStrike> rocketStrikesFromTwitter;
        try {
            rocketStrikesFromTwitter = findRocketStrikesInTwitter(region, sinceDate, untilDate);
            rocketStrikeRepository.saveAll(rocketStrikesFromTwitter);
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }

    private Set<RocketStrike> findRocketStrikesInTwitter(Region region,
                                                         LocalDate sinceDate, LocalDate untilDate) {
        Set<RocketStrike> rocketStrikes = new HashSet<>();
        try {
            Twitter authObject = TwitterAuth.authenticate();
            Query query = new Query();
            query.setQuery(formQueries(region, sinceDate, untilDate));
            QueryResult result = authObject.search(query);
            rocketStrikes.addAll(mapRocketStrike(result.getTweets(), region));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.debug("ROCKET STRIKES");
        for (RocketStrike rocketStrike:rocketStrikes) {
            log.debug(rocketStrike);
        }
        return rocketStrikes;
    }

    private Set<RocketStrike> mapRocketStrike(List<Status> tweets, Region region){
        return tweets.stream().map(tweet -> {
            RocketStrike rocketStrike= new RocketStrike();
            rocketStrike.setRegion(region);
            Date input = tweet.getCreatedAt();
            Instant instant = input.toInstant();
            ZonedDateTime zdt = instant.atZone(ZoneId.of("Europe/Kiev"));
            LocalDateTime date = zdt.toLocalDateTime();
            rocketStrike.setStrikeDate(date);
            log.debug(rocketStrike);
            return rocketStrike;
        }).collect(Collectors.toSet());
    }

    private String formQueries(Region region, LocalDate sinceDate, LocalDate untilDate){
        String usersQuery = FROM + String.join(OR + FROM, USERS);
        String keywordsQuery = String.join(OR, KEYWORDS);
        log.debug( usersQuery + SINCE + sinceDate + UNTIL + untilDate + formRegionQuery(region) + AND + keywordsQuery);
        return usersQuery + SINCE + sinceDate + UNTIL + untilDate + formRegionQuery(region) + AND + keywordsQuery;
    }

    private String formRegionQuery(Region region){
        String formattedRegionName = region.getRegionName().substring(0, region.getRegionName().length() - REGION_NAME_SUBS_NUMBER)
                + REGION_NAME_NEW_END;
        String formattedAltRegionName = region.getRegionAltName().substring(0, region.getRegionAltName().length() -
                REGION_ALT_NAME_SUBS_NUMBER) + REGION_ALT_NAME_NEW_END;
        return " " + formattedRegionName + OR + formattedAltRegionName;
    }


}
