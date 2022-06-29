package com.example.avengatwitterweatherapp.controller;

import com.example.avengatwitterweatherapp.dto.RocketStrikeDto;
import com.example.avengatwitterweatherapp.exceptions.BadDateRangeException;
import com.example.avengatwitterweatherapp.model.Region;
import com.example.avengatwitterweatherapp.model.RocketStrike;
import com.example.avengatwitterweatherapp.service.RegionService;
import com.example.avengatwitterweatherapp.service.RocketStrikeService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
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

    @GetMapping("/mvc")
    public String viewRecentRocketStrikes(Model model){

        List<RocketStrike> recentRocketStrikes = rocketStrikeService.getRecentRocketStrikes(SORT_BY_REGION, ASC_ORDER);
        model.addAttribute("listRocketStrikes", recentRocketStrikes);

        model.addAttribute("listRegions", regionService.getAllRegions());
        model.addAttribute("listSortFields", SORT_FIELDS);
        model.addAttribute("listSortDir", SORT_DIR);

        return "index.html";
    }

    @GetMapping("/rest")
    @ResponseBody
    public List<RocketStrike> viewRecentRocketStrikes(){
        return rocketStrikeService.getRecentRocketStrikes(SORT_BY_REGION, ASC_ORDER);
    }


    @PostMapping("/mvc/getFilteredRocketStrikes")
    public String viewFilteredRocketStrikes(@RequestParam String sinceDate,
                                            @RequestParam String untilDate,
                                            @RequestParam List<Long> checkedRegionsId,
                                            @RequestParam String sortField,
                                            @RequestParam String sortDir,
                                            Model model) {

        List<Region> checkedRegions = regionService.getRegionsById(checkedRegionsId);
        List<RocketStrike> filteredRocketStrikes = rocketStrikeService.getFilteredRocketStrikes(sinceDate,
                    untilDate, checkedRegions, sortField, sortDir);


        model.addAttribute("listRocketStrikes", filteredRocketStrikes);
        model.addAttribute("listRegions", regionService.getAllRegions());
        model.addAttribute("listSortFields", SORT_FIELDS);
        model.addAttribute("listSortDir", SORT_DIR);

        return "show_rocket_strikes.html";
    }


    @PostMapping("/rest/getFilteredRocketStrikes")
    @ResponseBody
    public List<RocketStrike> viewFilteredRocketStrikes(@RequestBody @Valid RocketStrikeDto rocketStrikeDto) {
        return rocketStrikeService.getFilteredRocketStrikes(rocketStrikeDto);
    }


}
