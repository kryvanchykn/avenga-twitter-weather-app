package com.example.avengatwitterweatherapp.controller;

import com.example.avengatwitterweatherapp.model.RocketStrike;
import com.example.avengatwitterweatherapp.service.RocketStrikeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashSet;

import static com.example.avengatwitterweatherapp.constants.RocketStrikeConstants.*;
import static com.example.avengatwitterweatherapp.constants.TwitterConstants.SINCE_DATE;
import static com.example.avengatwitterweatherapp.constants.TwitterConstants.UNTIL_DATE;

@Controller
public class RocketStrikeController {
    private final RocketStrikeService rocketStrikeService;

    public RocketStrikeController(RocketStrikeService rocketStrikeService) {
        this.rocketStrikeService = rocketStrikeService;
    }

    @GetMapping("/")
    public String viewRocketStrikes(Model model){
        model.addAttribute("setRocketStrikes", rocketStrikeService.getRocketStrikesFromTwitter());
    return sortedRocketStrikes(SORT_BY_REGION, ASC_ORDER, model);
    }


    @GetMapping("/sorted")
    public String sortedRocketStrikes(@RequestParam("sortField") String sortField,
                                      @RequestParam("sortDir") String sortDir,
                                      Model model) {

        HashSet<RocketStrike> setRocketStrikes = rocketStrikeService.getSortedRocketStrikes(sortField, sortDir);

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals(ASC_ORDER) ? DESC_ORDER : ASC_ORDER);

        model.addAttribute("setRocketStrikes", setRocketStrikes);
        return "rocket_strikes.html";
    }

    @PostMapping("/date")
    public String PostForm(@ModelAttribute("sinceDate") String sinceDate,
                           @ModelAttribute("untilDate") String untilDate,
                           Model model){
        System.out.println("sinceDate = " + sinceDate);
        return viewRocketStrikes(model);
    }
}
