package com.example.avengatwitterweatherapp.controllerTest;

import com.example.avengatwitterweatherapp.controller.RocketStrikeController;
import com.example.avengatwitterweatherapp.dto.RocketStrikeDto;
import com.example.avengatwitterweatherapp.model.Region;
import com.example.avengatwitterweatherapp.model.RocketStrike;
import com.example.avengatwitterweatherapp.service.RegionService;
import com.example.avengatwitterweatherapp.service.RocketStrikeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import static com.example.avengatwitterweatherapp.constants.RocketStrikeConstants.*;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RocketStrikeController.class)
public class RocketStrikeControllerTest {
    private static final Region REGION = new Region(1, "Львівська область", "Львівщина", "Lviv", "Lviv");
    private static final RocketStrike ROCKET_STRIKE = new RocketStrike(1, REGION, LocalDateTime.of(2022, 6, 25, 0, 0, 0));
    private static final LocalDateTime SINCE_DATE = LocalDateTime.of(2022, 6, 20, 0, 0, 0);
    private static final LocalDateTime UNTIL_DATE = LocalDateTime.of(2022, 6, 28, 0, 0, 0);
    private static final DateTimeFormatter MVC_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RocketStrikeService rocketStrikeService;

    @MockBean
    private RegionService regionService;

    @Test
    public void viewRecentRocketStrikesMVCTest() throws Exception {
        String formatDateTime = ROCKET_STRIKE.getStrikeDate().format(MVC_DATE_TIME_FORMATTER);

        when(rocketStrikeService.getRecentRocketStrikes(SORT_BY_REGION, ASC_ORDER)).thenReturn(List.of(ROCKET_STRIKE));

        this.mockMvc.perform(get("/mvc/")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8)))
                .andExpect(content().string(containsString(formatDateTime)))
                .andExpect(content().string(containsString(REGION.getRegionalCentre())));
    }

    @Test
    public void viewRecentRocketStrikesRESTTest() throws Exception {
        when(rocketStrikeService.getRecentRocketStrikes(SORT_BY_REGION, ASC_ORDER)).thenReturn(List.of(ROCKET_STRIKE));

        this.mockMvc.perform(get("/rest/")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString(ROCKET_STRIKE.getStrikeDate().toString())))
                .andExpect(content().string(containsString(REGION.getRegionalCentre())));
    }

    @Test
    public void viewFilteredRocketStrikesMVCTest() throws Exception {
        String formatDateTime = ROCKET_STRIKE.getStrikeDate().format(MVC_DATE_TIME_FORMATTER);

        when(regionService.getRegionsById(List.of(REGION.getId()))).thenReturn(List.of(REGION));
        when(rocketStrikeService.getFilteredRocketStrikes(SINCE_DATE, UNTIL_DATE, List.of(REGION),
                SORT_BY_REGION, ASC_ORDER)).thenReturn(List.of(ROCKET_STRIKE));

        this.mockMvc.perform(post("/mvc/getFilteredRocketStrikes")
                        .param("sinceDate", SINCE_DATE.toString())
                        .param("untilDate", UNTIL_DATE.toString())
                        .param("checkedRegionsId", String.valueOf(REGION.getId()))
                        .param("sortField", SORT_BY_REGION)
                        .param("sortDir", ASC_ORDER))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8)))
                .andExpect(content().string(containsString(formatDateTime)))
                .andExpect(content().string(containsString(REGION.getRegionalCentre())));
    }

    @Test
    public void viewFilteredRocketStrikesRESTTest() throws Exception {
        RocketStrikeDto rocketStrikeDto = new RocketStrikeDto();
        List<Long> checkedRegionsId = List.of(1L);

        rocketStrikeDto.setSinceDate(LocalDateTime.parse(SINCE_DATE.format(STRIKE_DATE_FORMATTER)));
        rocketStrikeDto.setUntilDate(LocalDateTime.parse(UNTIL_DATE.format(STRIKE_DATE_FORMATTER)));
        rocketStrikeDto.setCheckedRegionsId(checkedRegionsId);
        rocketStrikeDto.setSortField(SORT_BY_REGION);
        rocketStrikeDto.setSortDir(ASC_ORDER);

        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        String requestJson = mapper.writeValueAsString(rocketStrikeDto);

        when(regionService.getAllRegions()).thenReturn(List.of(REGION));
        when(rocketStrikeService.getFilteredRocketStrikes(rocketStrikeDto)).thenReturn(List.of(ROCKET_STRIKE));

        mockMvc.perform(MockMvcRequestBuilders.post("/rest/getFilteredRocketStrikes")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void viewFilteredRocketStrikesRESTBadCheckedRegionsIdTest() throws Exception {
        String errorMessage = "The input list should contain valid regions' id";
        RocketStrikeDto rocketStrikeDto = new RocketStrikeDto();
        List<Long> checkedRegionsId = List.of(new Random().nextLong());

        rocketStrikeDto.setSinceDate(LocalDateTime.parse(SINCE_DATE.format(STRIKE_DATE_FORMATTER)));
        rocketStrikeDto.setUntilDate(LocalDateTime.parse(UNTIL_DATE.format(STRIKE_DATE_FORMATTER)));
        rocketStrikeDto.setCheckedRegionsId(checkedRegionsId);
        rocketStrikeDto.setSortField(SORT_BY_REGION);
        rocketStrikeDto.setSortDir(ASC_ORDER);

        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        String requestJson = mapper.writeValueAsString(rocketStrikeDto);

        when(regionService.getAllRegions()).thenReturn(List.of(REGION));
        when(rocketStrikeService.getFilteredRocketStrikes(rocketStrikeDto)).thenReturn(List.of(ROCKET_STRIKE));

        mockMvc.perform(MockMvcRequestBuilders.post("/rest/getFilteredRocketStrikes")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(errorMessage)));
    }

    @Test
    public void viewFilteredRocketStrikesRESTBadDateRangeTest() throws Exception {
        String errorMessage = "sinceDate should be before untilDate";
        RocketStrikeDto rocketStrikeDto = new RocketStrikeDto();
        List<Long> checkedRegionsId = List.of(REGION.getId());

        rocketStrikeDto.setSinceDate(LocalDateTime.parse(UNTIL_DATE.format(STRIKE_DATE_FORMATTER)));
        rocketStrikeDto.setUntilDate(LocalDateTime.parse(SINCE_DATE.format(STRIKE_DATE_FORMATTER)));
        rocketStrikeDto.setCheckedRegionsId(checkedRegionsId);
        rocketStrikeDto.setSortField(SORT_BY_REGION);
        rocketStrikeDto.setSortDir(ASC_ORDER);

        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        String requestJson = mapper.writeValueAsString(rocketStrikeDto);

        when(regionService.getAllRegions()).thenReturn(List.of(REGION));
        when(rocketStrikeService.getFilteredRocketStrikes(rocketStrikeDto)).thenReturn(List.of(ROCKET_STRIKE));

        mockMvc.perform(MockMvcRequestBuilders.post("/rest/getFilteredRocketStrikes")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(errorMessage)));
    }
}