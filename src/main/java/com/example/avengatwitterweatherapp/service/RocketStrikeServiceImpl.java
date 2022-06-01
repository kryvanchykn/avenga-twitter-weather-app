package com.example.avengatwitterweatherapp.service;

import com.example.avengatwitterweatherapp.model.RocketStrike;

import com.example.avengatwitterweatherapp.twitter.TwitterAuth;
import com.example.avengatwitterweatherapp.twitter.TwitterData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import twitter4j.Twitter;
import java.util.*;

import static com.example.avengatwitterweatherapp.twitter.TwitterConstants.*;

@Service
public class RocketStrikeServiceImpl implements RocketStrikeService{
    @Autowired
    private RegionService regionService;

    @Override
    public HashSet<RocketStrike> getAllRocketStrikes() {
        Twitter authObject = null;
        HashSet<RocketStrike> rocketStrikes = new HashSet<>();
        try {
            authObject = TwitterAuth.authenticate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            rocketStrikes = TwitterData.getRocketStrikes(authObject, USERS, regionService.getAllRegions(),
                    KEYWORDS, SINCE_DATE, UNTIL_DATE);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return rocketStrikes;
    }


    @Override
    public Page<RocketStrike> getSortedRocketStrikes(String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

//        return this.employeeRepository.findAll(sort);
        return null;
    }
}
