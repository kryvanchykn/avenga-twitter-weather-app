package com.example.avengatwitterweatherapp.controller;

import com.example.avengatwitterweatherapp.model.RocketStrike;
import com.example.avengatwitterweatherapp.service.RocketStrikeService;
import com.example.avengatwitterweatherapp.utils.RocketStrikeTimeFilter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.example.avengatwitterweatherapp.constants.RocketStrikeConstants.*;
import static com.example.avengatwitterweatherapp.constants.TwitterConstants.SINCE_DATE;

@Controller
public class RocketStrikeController {
    private static final Logger log = LogManager.getLogger(RocketStrikeController.class);
    private final RocketStrikeService rocketStrikeService;

    public RocketStrikeController(RocketStrikeService rocketStrikeService) {
        this.rocketStrikeService = rocketStrikeService;
    }

    @GetMapping("/")
    public String viewRecentRocketStrikes(@RequestParam(name="sortField", defaultValue=SORT_BY_REGION) String sortField,
                                          @RequestParam(name="sortDir", defaultValue=ASC_ORDER) String sortDir,
                                          Model model){

        LocalDateTime sinceDate = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime untilDate = LocalDateTime.now().plusDays(1);
        List<RocketStrike> rocketStrikesFromDB = new ArrayList<>();

        while(rocketStrikesFromDB.size() == 0 && !sinceDate.equals(SINCE_DATE)){
            rocketStrikesFromDB = rocketStrikeService.getSortedRocketStrikesFromDB(sinceDate, untilDate, sortField, sortDir);
            log.debug("since to:  " + sinceDate.toLocalDate() + ' ' + untilDate.toLocalDate());
            log.debug("today rocket strikes number:  " + rocketStrikesFromDB.size());

            if(rocketStrikesFromDB.size() == 0){
                rocketStrikeService.saveRocketStrikesFromTwitter(sinceDate.toLocalDate(), untilDate.toLocalDate());
                model.addAttribute("listRocketStrikes",
                        rocketStrikeService.getSortedRocketStrikesFromDB(sinceDate, untilDate, sortField, sortDir));
            } else {
                model.addAttribute("listRocketStrikes", rocketStrikesFromDB);
            }
            sinceDate = sinceDate.minusDays(1);
            untilDate = untilDate.minusDays(1);
        }


        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals(ASC_ORDER) ? DESC_ORDER : ASC_ORDER);

        return "index.html";
    }


    @PostMapping("/getByDate")
    public String viewRocketStrikesByDate(@ModelAttribute("sinceDate") String sinceDateStr,
                                          @ModelAttribute("untilDate") String untilDateStr,
                                          @RequestParam(name="sortField", defaultValue=SORT_BY_REGION) String sortField,
                                          @RequestParam(name="sortDir", defaultValue=ASC_ORDER) String sortDir,
                                          Model model) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime sinceDate = LocalDateTime.parse(sinceDateStr, formatter);
        LocalDateTime untilDate = LocalDateTime.parse(untilDateStr, formatter);

        List<RocketStrike> rocketStrikesFromDB = rocketStrikeService.getSortedRocketStrikesFromDB(sinceDate, untilDate, sortField, sortDir);
        LocalDateTime firstRocketStrikeDate = rocketStrikeService.getFirstRocketStrikeDate();
        LocalDateTime lastRocketStrikeDate = rocketStrikeService.getLastRocketStrikeDate();

        if (rocketStrikesFromDB.size() == 0)
            rocketStrikeService.saveRocketStrikesFromTwitter(sinceDate.toLocalDate(), untilDate.toLocalDate());
        else {
            if (firstRocketStrikeDate.isAfter(sinceDate)) {
                rocketStrikeService.saveRocketStrikesFromTwitter(sinceDate.toLocalDate(), firstRocketStrikeDate.toLocalDate());
                log.debug(1);
            }
            if (lastRocketStrikeDate.isBefore(untilDate)) {
                rocketStrikeService.saveRocketStrikesFromTwitter(lastRocketStrikeDate.toLocalDate(), untilDate.toLocalDate());
                log.debug(2);
            }
        }

        List<RocketStrike> rocketStrikesBetweenDates = rocketStrikeService.getSortedRocketStrikesFromDB(sinceDate, untilDate.plusDays(1), sortField, sortDir);
        for(RocketStrike rs: rocketStrikesBetweenDates){
            log.debug(rs);
        }
        model.addAttribute("listRocketStrikes", RocketStrikeTimeFilter.filterByTime(sinceDate, untilDate, rocketStrikesBetweenDates));
        return "show_rocket_strikes.html";
    }


}
