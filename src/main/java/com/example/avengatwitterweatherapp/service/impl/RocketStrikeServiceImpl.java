package com.example.avengatwitterweatherapp.service.impl;

import com.example.avengatwitterweatherapp.model.Region;
import com.example.avengatwitterweatherapp.model.RocketStrike;
import com.example.avengatwitterweatherapp.repository.RocketStrikeRepository;
import com.example.avengatwitterweatherapp.service.RegionService;
import com.example.avengatwitterweatherapp.service.RocketStrikeService;
import com.example.avengatwitterweatherapp.twitter.TwitterAuth;
import com.example.avengatwitterweatherapp.utils.RocketStrikeTimeFilter;
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
    public List<RocketStrike> getRecentRocketStrikes(String sortField, String sortDir) {
        LocalDateTime sinceDate = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime untilDate = LocalDateTime.now().plusDays(1);
        List<RocketStrike> rocketStrikesFromDB = new ArrayList<>();
        List<RocketStrike> recentRocketStrikes = new ArrayList<>();

        while(rocketStrikesFromDB.size() == 0 && !sinceDate.equals(SINCE_DATE)){
            rocketStrikesFromDB = getRocketStrikesFromDB(sinceDate, untilDate, regionService.getAllRegions(),
                    sortField, sortDir);

            if(rocketStrikesFromDB.size() == 0) {
                saveRocketStrikesFromTwitter(sinceDate.toLocalDate(), untilDate.toLocalDate(), regionService.getAllRegions());
                rocketStrikesFromDB = getRocketStrikesFromDB(sinceDate, untilDate, regionService.getAllRegions(),
                        sortField, sortDir);
            }
            recentRocketStrikes =  rocketStrikesFromDB;

            log.debug("since to:  " + sinceDate.toLocalDate() + ' ' + untilDate.toLocalDate());
            log.debug("today rocket strikes number:  " + rocketStrikesFromDB.size());

            sinceDate = sinceDate.minusDays(1);
            untilDate = untilDate.minusDays(1);
        }
        return recentRocketStrikes;
    }

    @Override
    public List<RocketStrike> getFilteredRocketStrikes(String sinceDateStr, String untilDateStr, List<Region> regions,
                                                       String sortField, String sortDir) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime sinceDate = LocalDateTime.parse(sinceDateStr, formatter);
        LocalDateTime untilDate = LocalDateTime.parse(untilDateStr, formatter);

        List<RocketStrike> rocketStrikesFromDB = getRocketStrikesFromDB(sinceDate, untilDate, regions, sortField, sortDir);
        LocalDateTime firstRocketStrikeDate = getFirstRocketStrikeDate();
        LocalDateTime lastRocketStrikeDate = getLastRocketStrikeDate();

        if (rocketStrikesFromDB.size() == 0)
            saveRocketStrikesFromTwitter(sinceDate.toLocalDate(), untilDate.toLocalDate(), regions);
        else {
            if (firstRocketStrikeDate.isAfter(sinceDate)) {
                saveRocketStrikesFromTwitter(sinceDate.toLocalDate(), firstRocketStrikeDate.toLocalDate(), regions);
                log.debug(1);
            }
            if (lastRocketStrikeDate.isBefore(untilDate)) {
                saveRocketStrikesFromTwitter(lastRocketStrikeDate.toLocalDate(), untilDate.toLocalDate(), regions);
                log.debug(2);
            }
        }

        List<RocketStrike> rocketStrikesBetweenDates = getRocketStrikesFromDB(sinceDate, untilDate.plusDays(1),
                regions, sortField, sortDir);
        for(RocketStrike rs: rocketStrikesBetweenDates){
            log.debug(rs);
        }

        return RocketStrikeTimeFilter.filterByTime(sinceDate, untilDate, rocketStrikesBetweenDates);
    }

    @Override
    public void saveRocketStrikesFromTwitter(LocalDate sinceDate, LocalDate untilDate, List<Region> regions) {
        Set<RocketStrike> rocketStrikes;
        try {
            rocketStrikes = findRocketStrikesInTwitter(regions, sinceDate, untilDate);
            rocketStrikeRepository.saveAll(rocketStrikes);
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }

    @Override
    public List<RocketStrike> getRocketStrikesFromDB(LocalDateTime sinceDate, LocalDateTime untilDate,
                                                           List<Region> regions, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        List<RocketStrike> sortedRocketStrikesFromDB = new ArrayList<>();
        for (Region region: regions) {
            sortedRocketStrikesFromDB.addAll(rocketStrikeRepository.findRocketStrikeByStrikeDateBetweenAndRegion(sinceDate,
                    untilDate, region, sort));
        }

        return sortedRocketStrikesFromDB;
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

    @Override
    public RocketStrike getRocketStrikeById(Long id) {
        return rocketStrikeRepository.findById(id).get();
    }

    public Set<RocketStrike> findRocketStrikesInTwitter(List<Region> regions,
                                                        LocalDate sinceDate, LocalDate untilDate) {
        Set<RocketStrike> rocketStrikes = new HashSet<>();
        try {
            Twitter authObject = TwitterAuth.authenticate();
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
