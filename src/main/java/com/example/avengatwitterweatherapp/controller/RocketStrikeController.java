package com.example.avengatwitterweatherapp.controller;

import com.example.avengatwitterweatherapp.model.RocketStrike;
import com.example.avengatwitterweatherapp.repository.RocketStrikeRepository;
import com.example.avengatwitterweatherapp.service.RocketStrikeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.avengatwitterweatherapp.constants.RocketStrikeConstants.*;

@Controller
public class RocketStrikeController {
    private final RocketStrikeService rocketStrikeService;
    private final RocketStrikeRepository rocketStrikeRepository;

    public RocketStrikeController(RocketStrikeService rocketStrikeService, RocketStrikeRepository rocketStrikeRepository) {
        this.rocketStrikeService = rocketStrikeService;
        this.rocketStrikeRepository = rocketStrikeRepository;
    }

    @GetMapping("/")
    public String viewRocketStrikes(@RequestParam(name="sortField", defaultValue=SORT_BY_REGION) String sortField,
                                    @RequestParam(name="sortDir", defaultValue=ASC_ORDER) String sortDir,
                                    Model model){

        if(model.containsAttribute("setRocketStrikesBetweenDates")){
            System.out.println("Dates");
            model.addAttribute("setRocketStrikes", model.getAttribute("setRocketStrikesBetweenDates"));

        } else {
            if(rocketStrikeService.getRocketStrikesFromDB().size() == 0){
                rocketStrikeService.getRocketStrikesFromTwitter();
            }
            List<RocketStrike> rocketStrikes = rocketStrikeService.getSortedRocketStrikes(sortField, sortDir);
            model.addAttribute("setRocketStrikes", rocketStrikes);
        }

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals(ASC_ORDER) ? DESC_ORDER : ASC_ORDER);

        return "rocket_strikes.html";
    }

    @PostMapping("/date")
    public String viewRocketStrikesByDate(@ModelAttribute("sinceDate") String sinceDate,
                                          @ModelAttribute("untilDate") String untilDate,
                                          Model model){
        model.addAttribute("setRocketStrikesBetweenDates",
                rocketStrikeRepository.findSortedRocketStrikeByStrikeDateBetween(sinceDate, untilDate));
        return viewRocketStrikes(SORT_BY_REGION, ASC_ORDER, model);
    }


}
