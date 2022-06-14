package com.example.avengatwitterweatherapp;

import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
public class AvengaTwitterWeatherAppApplication {

    public static void main(String[] args) {
        BasicConfigurator.configure();
        run(AvengaTwitterWeatherAppApplication.class, args);
    }

}
