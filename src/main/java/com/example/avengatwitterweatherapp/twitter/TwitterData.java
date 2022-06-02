package com.example.avengatwitterweatherapp.twitter;

import com.example.avengatwitterweatherapp.model.Region;
import com.example.avengatwitterweatherapp.model.RocketStrike;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;

import java.time.LocalDate;
import java.util.*;

import static com.example.avengatwitterweatherapp.constants.RegionConstants.*;
import static com.example.avengatwitterweatherapp.constants.TwitterConstants.*;

public class TwitterData {

    public static HashSet<RocketStrike> getRocketStrikes(Twitter authObject, Set<String> users, List<Region> regions,
                                                           Set<String> keywords, LocalDate sinceDate, LocalDate untilDate) {
        ArrayList<RocketStrike> rocketStrikes = new ArrayList<>();
        RocketStrike rocketStrike;
        long id = 1;
        try {
            Query query = new Query();
            QueryResult result;
            query.setSince("15-05-2022");
            query.setUntil("20-05-2022");
            for (Region region : regions) {
                query.setQuery(formQueries(users, region, keywords));
                result = authObject.search(query);
                List<Status> tweets = result.getTweets();
                for (Status tweet : tweets) {
                    System.out.println(tweet.getUser().getName() + "\n" + tweet.getCreatedAt() + "\n" + tweet.getText() + "\n\n");
                    rocketStrike = new RocketStrike();
                    rocketStrike.setStrikeDate(tweet.getCreatedAt());
                    rocketStrike.setRegion(region);
                    rocketStrike.setId(id++);
                    rocketStrikes.add(rocketStrike);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to search tweets: " + e.getMessage());
        }
        return new HashSet<>(rocketStrikes);
    }

    private static String formQueries(Set<String> users, Region region, Set<String> keywords){
        String usersQuery = FROM + String.join(OR + FROM, users);
        String regionQuery = formRegionQuery(region);
        String keywordsQuery = String.join( OR, keywords);
        System.out.println(usersQuery + AND + regionQuery + AND + keywordsQuery);
        return usersQuery + AND + regionQuery + AND + keywordsQuery;
    }

    private static String formRegionQuery(Region region){
        return region.getRegionName().substring(0, region.getRegionName().length() - REGION_NAME_SUBS_NUMBER)
                + REGION_NAME_NEW_END + OR + region.getRegionAltName().substring(0, region.getRegionAltName().length() -
                REGION_ALT_NAME_SUBS_NUMBER) + REGION_ALT_NAME_NEW_END;
    }

}
