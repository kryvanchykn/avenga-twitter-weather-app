package com.example.avengatwitterweatherapp.controller;

import com.example.avengatwitterweatherapp.model.Region;
import com.example.avengatwitterweatherapp.model.RocketStrike;
import com.example.avengatwitterweatherapp.dto.RocketStrikeDto;
import com.example.avengatwitterweatherapp.service.RegionService;
import com.example.avengatwitterweatherapp.service.RocketStrikeService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.example.avengatwitterweatherapp.constants.RocketStrikeConstants.*;

@RestController
public class RocketStrikeController {
    private static final Logger log = LogManager.getLogger(RocketStrikeController.class);
    private final RocketStrikeService rocketStrikeService;
    private final RegionService regionService;

    public RocketStrikeController(RocketStrikeService rocketStrikeService, RegionService regionService) {
        this.rocketStrikeService = rocketStrikeService;
        this.regionService = regionService;
    }

//    @GetMapping("/")
//    public String viewRecentRocketStrikes(Model model){
//
//        List<RocketStrike> recentRocketStrikes = rocketStrikeService.getRecentRocketStrikes(SORT_BY_REGION, ASC_ORDER);
//        model.addAttribute("listRocketStrikes", recentRocketStrikes);
//
//        model.addAttribute("listRegions", regionService.getAllRegions());
//        model.addAttribute("listSortFields", SORT_FIELDS);
//        model.addAttribute("listSortDir", SORT_DIR);
//
//        return "index.html";
//    }
//
//
//    @PostMapping("/getFilteredRocketStrikes")
//    public String viewFilteredRocketStrikes(@ModelAttribute("sinceDate") String sinceDate,
//                                            @ModelAttribute("untilDate") String untilDate,
//                                            @RequestParam("checkedRegions") List<Long> checkedRegionsId,
//                                            @RequestParam(name="sortField") String sortField,
//                                            @RequestParam(name="sortDir") String sortDir,
//                                            Model model) {
//        log.debug("SortField: " + sortField);
//        log.debug("SortDir: " + sortDir);
//        log.debug("sinceDate: " + sinceDate);
//
//        List<Region> checkedRegions = regionService.getRegionsById(checkedRegionsId);
//        List<RocketStrike> filteredRocketStrikes = rocketStrikeService.getFilteredRocketStrikes(sinceDate,
//                untilDate, checkedRegions, sortField, sortDir);
//
//        model.addAttribute("listRocketStrikes", filteredRocketStrikes);
//        model.addAttribute("listRegions", regionService.getAllRegions());
//        model.addAttribute("listSortFields", SORT_FIELDS);
//        model.addAttribute("listSortDir", SORT_DIR);
//
//        return "show_rocket_strikes.html";
//    }

    @GetMapping("/")
    public List<RocketStrike> viewRecentRocketStrikes(){
        return rocketStrikeService.getRecentRocketStrikes(SORT_BY_REGION, ASC_ORDER);
    }


    @PostMapping("/getFilteredRocketStrikes")
    public List<RocketStrike> viewFilteredRocketStrikes(@RequestBody @Valid RocketStrikeDto paramsWrapper) {
        paramsWrapper = rocketStrikeService.validateRocketStrikeParamsWrapper(paramsWrapper);
        List<Region> checkedRegions = regionService.getRegionsById(paramsWrapper.getCheckedRegionsId());
        return rocketStrikeService.getFilteredRocketStrikes(paramsWrapper.getSinceDate(),
                paramsWrapper.getUntilDate(), checkedRegions, paramsWrapper.getSortField(), paramsWrapper.getSortDir());
    }


}
