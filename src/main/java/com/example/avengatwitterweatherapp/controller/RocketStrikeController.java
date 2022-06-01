package com.example.avengatwitterweatherapp.controller;

import com.example.avengatwitterweatherapp.service.RocketStrikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RocketStrikeController {
    @Autowired
    private RocketStrikeService rocketStrikeService;

    @GetMapping("/")
    public String viewRocketStrikes(Model model){
        model.addAttribute("listRocketStrikes", rocketStrikeService.getAllRocketStrikes());
        return "rocket_strikes.html";
    }
}
