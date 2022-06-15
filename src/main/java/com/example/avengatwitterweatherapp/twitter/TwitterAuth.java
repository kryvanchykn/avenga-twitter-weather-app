package com.example.avengatwitterweatherapp.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterAuth {
    public static Twitter authenticate() {

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        cb.setJSONStoreEnabled(true);
        cb.setOAuthConsumerKey(System.getenv("CONSUMER_KEY2"));
        cb.setOAuthConsumerSecret(System.getenv("CONSUMER_SECRET2"));
        cb.setOAuthAccessToken(System.getenv("ACCESS_TOKEN2"));
        cb.setOAuthAccessTokenSecret(System.getenv("ACCESS_SECRET2"));
        cb.setHttpConnectionTimeout(100000);

        return new TwitterFactory(cb.build()).getInstance();

    }
}
