package com.example.avengatwitterweatherapp.service.impl;

import com.example.avengatwitterweatherapp.dto.RocketStrikeDto;
import com.example.avengatwitterweatherapp.exceptions.BadDateRangeException;
import com.example.avengatwitterweatherapp.exceptions.NoSelectedRegionsException;
import com.example.avengatwitterweatherapp.exceptions.RocketStrikeNotFoundException;
import com.example.avengatwitterweatherapp.model.Region;
import com.example.avengatwitterweatherapp.model.RocketStrike;
import com.example.avengatwitterweatherapp.repository.RocketStrikeRepository;
import com.example.avengatwitterweatherapp.service.RegionService;
import com.example.avengatwitterweatherapp.service.RocketStrikeService;
import com.example.avengatwitterweatherapp.service.TwitterService;
import org.apache.commons.beanutils.BeanComparator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.avengatwitterweatherapp.constants.RocketStrikeConstants.ASC_ORDER;
import static com.example.avengatwitterweatherapp.constants.RocketStrikeConstants.SORT_BY_REGION;
import static com.example.avengatwitterweatherapp.constants.TwitterConstants.BOUNDARY_DATE;

@Service
public class RocketStrikeServiceImpl implements RocketStrikeService {
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
        LocalDateTime sinceDate = LocalDateTime.now().with(LocalTime.MIN).truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime untilDate = sinceDate.plusDays(1);
        List<RocketStrike> recentRocketStrikes = new ArrayList<>();

        while (recentRocketStrikes.size() == 0 && !sinceDate.equals(BOUNDARY_DATE)) {
            recentRocketStrikes = getFilteredRocketStrikes(sinceDate, untilDate,
                    regionService.getAllRegions(), SORT_BY_REGION, ASC_ORDER);

            sinceDate = sinceDate.minusDays(1);
            untilDate = untilDate.minusDays(1);
        }
        return recentRocketStrikes;
    }

    @Override
    public List<RocketStrike> getFilteredRocketStrikes(LocalDateTime sinceDate, LocalDateTime untilDate, List<Region> regions,
                                                       String sortField, String sortDir) {

        if (untilDate.isAfter(LocalDateTime.now())) {
            untilDate = LocalDateTime.now().plusDays(1).with(LocalTime.MIN);
        }

        if (sinceDate.isAfter(untilDate)) {
            throw new BadDateRangeException("sinceDate should be before untilDate");
        }

        if (regions == null || regions.size() == 0) {
            throw new NoSelectedRegionsException("any region was selected");
        }


        LocalDateTime firstRocketStrikeDate;
        LocalDateTime lastRocketStrikeDate;

        List<RocketStrike> rocketStrikesFromDB = getRocketStrikesFromDB(sinceDate, untilDate, regions, sortField, sortDir);
        for (Region region : regions) {
            firstRocketStrikeDate = getFirstRocketStrikeDateRecord(region);
            lastRocketStrikeDate = getLastRocketStrikeDateRecord(region);

            if (rocketStrikesFromDB.size() == 0) {
                twitterService.saveRocketStrikesFromTwitter(sinceDate.toLocalDate(), untilDate.toLocalDate(), region);
            } else {
                if (firstRocketStrikeDate.isAfter(sinceDate)) {
                    twitterService.saveRocketStrikesFromTwitter(sinceDate.toLocalDate(), firstRocketStrikeDate.toLocalDate(), region);
                }
                if (lastRocketStrikeDate.isBefore(untilDate)) {
                    twitterService.saveRocketStrikesFromTwitter(lastRocketStrikeDate.toLocalDate(), untilDate.toLocalDate(), region);
                }
            }
        }

        List<RocketStrike> filteredRocketStrikes = getRocketStrikesFromDB(sinceDate, untilDate, regions, sortField, sortDir);
        return filterByTime(sinceDate, untilDate, filteredRocketStrikes);
    }

    @Override
    public List<RocketStrike> getFilteredRocketStrikes(RocketStrikeDto rocketStrikeDto) {
        List<Region> checkedRegions = regionService.getRegionsById(rocketStrikeDto.getCheckedRegionsId());
        return getFilteredRocketStrikes(rocketStrikeDto.getSinceDate(), rocketStrikeDto.getUntilDate(),
                checkedRegions, rocketStrikeDto.getSortField(), rocketStrikeDto.getSortDir());
    }

    @Override
    public RocketStrike getRocketStrikeById(long id) {
        Optional<RocketStrike> rocketStrikeByIdStream = rocketStrikeRepository.findById(id);
        if (rocketStrikeByIdStream.isEmpty()) {
            throw new RocketStrikeNotFoundException("Weather forecast for rocket strike with id=" + id + " is not found");
        }
        return rocketStrikeByIdStream.get();
    }


    public List<RocketStrike> getRocketStrikesFromDB(LocalDateTime sinceDate, LocalDateTime untilDate,
                                                     List<Region> regions, String sortField, String sortDirection) {
        Set<RocketStrike> rocketStrikeSet = new HashSet<>();
        for (Region region : regions) {
            rocketStrikeSet.addAll(rocketStrikeRepository.findRocketStrikeByStrikeDateBetweenAndRegion(sinceDate,
                    untilDate, region));
        }

        Comparator<RocketStrike> fieldCompare = new BeanComparator<>(sortField);
        List<RocketStrike> rocketStrikeList = new ArrayList<>(rocketStrikeSet.stream().toList());
        rocketStrikeList.sort(fieldCompare);
        if (!sortDirection.equals(ASC_ORDER)) {
            Collections.reverse(rocketStrikeList);
        }

        return rocketStrikeList;
    }

    private LocalDateTime getFirstRocketStrikeDateRecord(Region region) {
        RocketStrike rocketStrike = new RocketStrike();
        rocketStrike.setStrikeDate(LocalDateTime.now());
        RocketStrike rocketStrike1 = rocketStrikeRepository.findAll().stream()
                .filter(rs -> rs.getRegion().equals(region))
                .min(Comparator.comparing(RocketStrike::getStrikeDate))
                .orElse(rocketStrike);
        return rocketStrike1.getStrikeDate();
    }

    private LocalDateTime getLastRocketStrikeDateRecord(Region region) {
        RocketStrike rocketStrike = new RocketStrike();
        rocketStrike.setStrikeDate(BOUNDARY_DATE);
        RocketStrike rocketStrike1 = rocketStrikeRepository.findAll().stream()
                .filter(rs -> rs.getRegion().equals(region))
                .max(Comparator.comparing(RocketStrike::getStrikeDate))
                .orElse(rocketStrike);
        return rocketStrike1.getStrikeDate();
    }

    private List<RocketStrike> filterByTime(LocalDateTime sinceDate, LocalDateTime untilDate,
                                            List<RocketStrike> rocketStrikes) {
        return rocketStrikes.stream().filter(
                        rs -> rs.getStrikeDate().isAfter(sinceDate) &&
                                rs.getStrikeDate().isBefore(untilDate) ||
                                untilDate.equals(rs.getStrikeDate()) ||
                                sinceDate.equals(rs.getStrikeDate()))
                .collect(Collectors.toList());
    }
}
