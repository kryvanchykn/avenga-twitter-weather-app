package com.example.avengatwitterweatherapp;

import com.example.avengatwitterweatherapp.constants.ConfigProperties;
import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
@EnableConfigurationProperties(ConfigProperties.class)
@ConfigurationPropertiesScan("com.example.avengatwitterweatherapp.constants")
public class AvengaTwitterWeatherAppApplication {

    public static void main(String[] args) {
        BasicConfigurator.configure();
        run(AvengaTwitterWeatherAppApplication.class, args);
    }

}
