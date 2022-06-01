package com.example.avengatwitterweatherapp.service.impl;

import com.example.avengatwitterweatherapp.model.RocketStrike;

import com.example.avengatwitterweatherapp.repository.RocketStrikeRepository;
import com.example.avengatwitterweatherapp.service.RegionService;
import com.example.avengatwitterweatherapp.service.RocketStrikeService;
import com.example.avengatwitterweatherapp.twitter.TwitterAuth;
import com.example.avengatwitterweatherapp.twitter.TwitterData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import twitter4j.Twitter;
import java.util.*;

import static com.example.avengatwitterweatherapp.twitter.TwitterConstants.*;

@Service
public class RocketStrikeServiceImpl implements RocketStrikeService {
    private final RegionService regionService;
    private final RocketStrikeRepository rocketStrikeRepository;

    public RocketStrikeServiceImpl(RegionService regionService, RocketStrikeRepository rocketStrikeRepository) {
        this.regionService = regionService;
        this.rocketStrikeRepository = rocketStrikeRepository;
    }

    @Override
    public HashSet<RocketStrike> getAllRocketStrikes() {
        Twitter authObject;
        HashSet<RocketStrike> rocketStrikes = new HashSet<>();
        try {
            authObject = TwitterAuth.authenticate();
            rocketStrikes = TwitterData.getRocketStrikes(authObject, USERS, regionService.getAllRegions(),
                    KEYWORDS, SINCE_DATE, UNTIL_DATE);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        rocketStrikeRepository.saveAll(rocketStrikes);
        return rocketStrikes;
    }


    @Override
    public List<RocketStrike> getSortedRocketStrikes(String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        return rocketStrikeRepository.findAll(sort);
    }

//    @Override
//    public RocketStrike saveRocketStrike(RocketStrike rocketStrike) {
//        return rocketStrikeRepository.save(rocketStrike);
//    }
}
