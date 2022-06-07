package com.example.avengatwitterweatherapp.controller;

import com.example.avengatwitterweatherapp.service.RegionService;
import org.springframework.stereotype.Controller;

@Controller
public class RegionController {
    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

//    @GetMapping("/")
//    public String viewHomePage(Model model){
//        model.addAttribute("listRegions", regionService.getAllRegions());
//        return "index";
//    }
}
