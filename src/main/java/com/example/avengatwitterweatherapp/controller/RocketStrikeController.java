package com.example.avengatwitterweatherapp.controller;

import com.example.avengatwitterweatherapp.model.Region;
import com.example.avengatwitterweatherapp.model.RocketStrike;
import com.example.avengatwitterweatherapp.service.RegionService;
import com.example.avengatwitterweatherapp.service.RocketStrikeService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.example.avengatwitterweatherapp.constants.RocketStrikeConstants.*;

@Controller
public class RocketStrikeController {
    private static final Logger log = LogManager.getLogger(RocketStrikeController.class);
    private final RocketStrikeService rocketStrikeService;
    private final RegionService regionService;

    public RocketStrikeController(RocketStrikeService rocketStrikeService, RegionService regionService) {
        this.rocketStrikeService = rocketStrikeService;
        this.regionService = regionService;
    }

    @GetMapping("/")
    public String viewRecentRocketStrikes(@RequestParam(name="sortField", defaultValue=SORT_BY_REGION) String sortField,
                                          @RequestParam(name="sortDir", defaultValue=ASC_ORDER) String sortDir,
                                          Model model){

        List<RocketStrike> recentRocketStrikesFromDB = rocketStrikeService.getRecentRocketStrikes(sortField, sortDir);
        model.addAttribute("listRocketStrikes", recentRocketStrikesFromDB);

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals(ASC_ORDER) ? DESC_ORDER : ASC_ORDER);

        model.addAttribute("listRegions", regionService.getAllRegions());

        return "index.html";
    }


    @PostMapping("/getFilteredRocketStrikes")
    public String viewRocketStrikesByDate(@ModelAttribute("sinceDate") String sinceDate,
                                          @ModelAttribute("untilDate") String untilDate,
                                          @RequestParam("checkedRegions") List<Long> checkedRegionsId,
                                          @RequestParam(name="sortField", defaultValue=SORT_BY_REGION) String sortField,
                                          @RequestParam(name="sortDir", defaultValue=ASC_ORDER) String sortDir,
                                          Model model) {
        List<Region> checkedRegions = regionService.getRegionsById(checkedRegionsId);
        List<RocketStrike> rocketStrikesBetweenDates = rocketStrikeService.getFilteredRocketStrikes(sinceDate,
                untilDate, checkedRegions, sortField, sortDir);
        model.addAttribute("listRocketStrikes", rocketStrikesBetweenDates);
        model.addAttribute("listRegions", regionService.getAllRegions());

        return "show_rocket_strikes.html";
    }


}
