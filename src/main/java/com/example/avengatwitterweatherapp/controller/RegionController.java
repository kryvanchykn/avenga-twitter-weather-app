package com.example.avengatwitterweatherapp.controller;

import com.example.avengatwitterweatherapp.repository.RegionRepository;
import com.example.avengatwitterweatherapp.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegionController {
    @Autowired
    private RegionService regionService;

//    @GetMapping("/")
//    public String viewHomePage(Model model){
//        model.addAttribute("listRegions", regionService.getAllRegions());
//        return "index";
//    }
}
