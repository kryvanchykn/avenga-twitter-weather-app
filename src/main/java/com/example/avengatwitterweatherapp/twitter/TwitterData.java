package com.example.avengatwitterweatherapp.twitter;

import com.example.avengatwitterweatherapp.model.Region;
import com.example.avengatwitterweatherapp.model.RocketStrike;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;

import java.time.LocalDate;
import java.util.*;

public class TwitterData {

    public static HashSet<RocketStrike> getRocketStrikes(Twitter authObject, Set<String> users, List<Region> regions,
                                                           Set<String> keywords, LocalDate sinceDate, LocalDate untilDate) {
        ArrayList<RocketStrike> rocketStrikes = new ArrayList<>();
        RocketStrike rocketStrike;
        long id = 1;
        try {
            Query query = new Query();
            QueryResult result;
            query.setSince(sinceDate.toString());
            query.setUntil(untilDate.toString());
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
        String usersQuery = "(from:" + String.join(" OR from:", users) + ")";
        String regionQuery = "(" + region.getRegionName() + " OR " + region.getRegionAltName() + ")";
        String keywordsQuery = "(" + String.join( " OR ", keywords) + ")";
        System.out.println(usersQuery + " AND " + regionQuery + " AND " + keywordsQuery);
        return usersQuery + " AND " + regionQuery + " AND " + keywordsQuery;
    }

}
