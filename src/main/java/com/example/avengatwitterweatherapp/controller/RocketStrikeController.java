package com.example.avengatwitterweatherapp.controller;

import com.example.avengatwitterweatherapp.model.RocketStrike;
import com.example.avengatwitterweatherapp.service.RocketStrikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class RocketStrikeController {
    @Autowired
    private RocketStrikeService rocketStrikeService;

    @GetMapping("/")
    public String viewRocketStrikes(Model model){
        model.addAttribute("listRocketStrikes", rocketStrikeService.getAllRocketStrikes());
        return sortedRocketStrikes("region", "asc", model);
    }


    @GetMapping("/sorted")
    public String sortedRocketStrikes(@RequestParam("sortField") String sortField,
                                      @RequestParam("sortDir") String sortDir,
                                      Model model) {

        List<RocketStrike> listRocketStrikes = rocketStrikeService.getSortedRocketStrikes(sortField, sortDir);

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listRocketStrikes", listRocketStrikes);
        return "rocket_strikes.html";
    }
}
