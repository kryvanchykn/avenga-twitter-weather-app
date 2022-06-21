package com.example.avengatwitterweatherapp.service.impl;

import com.example.avengatwitterweatherapp.exceptions.RocketStrikeNotFoundException;
import com.example.avengatwitterweatherapp.model.Region;
import com.example.avengatwitterweatherapp.model.RocketStrike;
import com.example.avengatwitterweatherapp.dto.RocketStrikeDto;
import com.example.avengatwitterweatherapp.repository.RocketStrikeRepository;
import com.example.avengatwitterweatherapp.service.RegionService;
import com.example.avengatwitterweatherapp.service.RocketStrikeService;
import com.example.avengatwitterweatherapp.service.TwitterService;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.avengatwitterweatherapp.constants.RocketStrikeConstants.ASC_ORDER;
import static com.example.avengatwitterweatherapp.constants.RocketStrikeConstants.SORT_BY_REGION;
import static com.example.avengatwitterweatherapp.constants.TwitterConstants.*;

@Service
public class RocketStrikeServiceImpl implements RocketStrikeService {
    private static final Logger log = LogManager.getLogger(RocketStrikeServiceImpl.class);

    private final RegionService regionService;
    private final TwitterService twitterService;
    private final RocketStrikeRepository rocketStrikeRepository;


    public RocketStrikeServiceImpl(RegionService regionService, TwitterService twitterService, RocketStrikeRepository rocketStrikeRepository) {
        this.regionService = regionService;
        this.twitterService = twitterService;
        this.rocketStrikeRepository = rocketStrikeRepository;
    }

    @Override
    public List<RocketStrike> getRecentRocketStrikes(String sortField, String sortDir) {
        LocalDateTime sinceDate = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime untilDate = LocalDateTime.now().plusDays(1).minusSeconds(1);
        List<RocketStrike> recentRocketStrikes = new ArrayList<>();

        while(recentRocketStrikes.size() == 0 && !sinceDate.equals(SINCE_DATE)){
            recentRocketStrikes = getFilteredRocketStrikes(sinceDate.truncatedTo(ChronoUnit.MINUTES).toString(),
                    untilDate.truncatedTo(ChronoUnit.MINUTES).toString(),
                    regionService.getAllRegions(), SORT_BY_REGION, ASC_ORDER);

            log.debug("since to:  " + sinceDate.toLocalDate() + ' ' + untilDate.toLocalDate());
            log.debug("today rocket strikes number:  " + recentRocketStrikes.size());

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
        LocalDateTime untilDate = LocalDateTime.parse(untilDateStr, formatter).isAfter(LocalDateTime.now()) ?
                LocalDateTime.now() : LocalDateTime.parse(untilDateStr, formatter);

        LocalDateTime firstRocketStrikeDate;
        LocalDateTime lastRocketStrikeDate;

        List<RocketStrike> rocketStrikesFromDB = getRocketStrikesFromDB(sinceDate, untilDate, regions, sortField, sortDir);
        for (Region region: regions) {
            firstRocketStrikeDate = getFirstRocketStrikeDateRecord(region);
            lastRocketStrikeDate = getLastRocketStrikeDateRecord(region);

            if (rocketStrikesFromDB.size() == 0)
                twitterService.saveRocketStrikesFromTwitter(sinceDate.toLocalDate(), untilDate.toLocalDate(), region);
            else {
                if (firstRocketStrikeDate.isAfter(sinceDate)) {
                    twitterService.saveRocketStrikesFromTwitter(sinceDate.toLocalDate(), firstRocketStrikeDate.toLocalDate(), region);
                    log.debug(1);
                }
                if (lastRocketStrikeDate.isBefore(untilDate)) {
                    twitterService.saveRocketStrikesFromTwitter(lastRocketStrikeDate.toLocalDate(), untilDate.toLocalDate(), region);
                    log.debug(2);
                }
            }
        }


        List<RocketStrike> filteredRocketStrikes = getRocketStrikesFromDB(sinceDate, untilDate.plusDays(1),
                regions, sortField, sortDir);
        for(RocketStrike rs: filteredRocketStrikes){
            log.debug(rs);
        }

        return filterByTime(sinceDate, untilDate, filteredRocketStrikes);
    }



    @Override
    public List<RocketStrike> getRocketStrikesFromDB(LocalDateTime sinceDate, LocalDateTime untilDate,
                                                           List<Region> regions, String sortField, String sortDirection) {
        List<RocketStrike> sortedRocketStrikesFromDB = new ArrayList<>();
        for (Region region: regions) {
            sortedRocketStrikesFromDB.addAll(rocketStrikeRepository.findRocketStrikeByStrikeDateBetweenAndRegion(sinceDate,
                    untilDate, region));
        }

        Comparator<RocketStrike> fieldCompare = new BeanComparator<>(sortField);
        sortedRocketStrikesFromDB.sort(fieldCompare);
        if(!sortDirection.equals(ASC_ORDER)){
            Collections.reverse(sortedRocketStrikesFromDB);
        }

        return sortedRocketStrikesFromDB;
    }

    @Override
    public LocalDateTime getFirstRocketStrikeDateRecord(Region region) {
        RocketStrike rocketStrike = new RocketStrike();
        rocketStrike.setStrikeDate(UNTIL_DATE);
        return rocketStrikeRepository.findAll().stream()
                .filter(rs -> rs.getRegion().equals(region))
                .min(Comparator.comparing(RocketStrike::getStrikeDate))
                .orElse(rocketStrike).getStrikeDate();
    }

    @Override
    public LocalDateTime getLastRocketStrikeDateRecord(Region region) {
        RocketStrike rocketStrike = new RocketStrike();
        rocketStrike.setStrikeDate(SINCE_DATE);
        return rocketStrikeRepository.findAll().stream()
                .filter(rs -> rs.getRegion().equals(region))
                .max(Comparator.comparing(RocketStrike::getStrikeDate))
                .orElse(rocketStrike).getStrikeDate();
    }

    @Override
    public RocketStrike getRocketStrikeById(Long id) {
        try{
            return rocketStrikeRepository.findById(id).get();
        } catch(NoSuchElementException ex){
            log.error(ex.getMessage());
            throw new RocketStrikeNotFoundException("Weather forecast for rocket strike with id=" + id + " is not found",
                    ex);
        }
    }

    @Override
    public RocketStrikeDto setDefaultForRocketStrikeDto(RocketStrikeDto paramsWrapper){
        if (paramsWrapper.getSinceDate() == null) {
            paramsWrapper.setSinceDate(String.valueOf(SINCE_DATE));
        }
        if (paramsWrapper.getUntilDate() == null) {
            paramsWrapper.setUntilDate(String.valueOf(UNTIL_DATE.truncatedTo(ChronoUnit.MINUTES)));
        }
        if (paramsWrapper.getCheckedRegionsId() == null) {
            paramsWrapper.setCheckedRegionsId(regionService.getAllRegions().stream().map(Region::getId)
                    .collect(Collectors.toList()));
        }
        if (paramsWrapper.getSortField() == null) {
            paramsWrapper.setSortField(SORT_BY_REGION);
        }
        if (paramsWrapper.getSortDir() == null) {
            paramsWrapper.setSortDir(ASC_ORDER);
        }
        return paramsWrapper;
    }

    private List<RocketStrike> filterByTime(LocalDateTime sinceDate, LocalDateTime untilDate,
                                            List<RocketStrike> rocketStrikes){
        return rocketStrikes.stream().filter(rocketStrike -> {
            boolean b = rocketStrike.getStrikeDate().isAfter(sinceDate) &&
                    rocketStrike.getStrikeDate().isBefore(untilDate);
            log.debug(rocketStrike.getStrikeDate());
            return b;
        }).collect(Collectors.toList());
    }
}
