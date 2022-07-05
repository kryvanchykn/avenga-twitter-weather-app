package com.example.avengatwitterweatherapp.controllerTest;

import com.example.avengatwitterweatherapp.dto.RocketStrikeDto;
import com.example.avengatwitterweatherapp.model.Region;
import com.example.avengatwitterweatherapp.model.RocketStrike;
import com.example.avengatwitterweatherapp.service.RocketStrikeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.example.avengatwitterweatherapp.constants.RocketStrikeConstants.*;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RocketStrikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RocketStrikeService rocketStrikeService;

    @Test
    public void viewRecentRocketStrikesMVCTest() throws Exception {
        Region region1 = new Region(1, "Львівщина", "Львівська область", "Lviv", "Lviv");
        RocketStrike rocketStrike1 = new RocketStrike(1, region1, LocalDateTime.now());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
        String formatDateTime = rocketStrike1.getStrikeDate().format(formatter);

        when(rocketStrikeService.getRecentRocketStrikes(SORT_BY_REGION, ASC_ORDER)).thenReturn(List.of(rocketStrike1));

        this.mockMvc.perform(get("/mvc/")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8)))
                .andExpect(content().string(containsString(formatDateTime)))
                .andExpect(content().string(containsString(region1.getRegionalCentre())));
    }

    @Test
    public void viewRecentRocketStrikesRESTTest() throws Exception {
        Region region1 = new Region(1, "Львівщина", "Львівська область", "Lviv", "Lviv");
        RocketStrike rocketStrike1 = new RocketStrike(1, region1,  LocalDateTime.of(2022, 6, 25, 0, 0, 0));

        when(rocketStrikeService.getRecentRocketStrikes(SORT_BY_REGION, ASC_ORDER)).thenReturn(List.of(rocketStrike1));

        this.mockMvc.perform(get("/rest/")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString(rocketStrike1.getStrikeDate().toString())))
                .andExpect(content().string(containsString(region1.getRegionalCentre())));
    }

    @Test
    public void viewFilteredRocketStrikesMVCTest() throws Exception {
        Region region1 = new Region(1, "Львівська область", "Львівщина", "Lviv", "Lviv");
        RocketStrike rocketStrike1 = new RocketStrike(1, region1, LocalDateTime.of(2022, 6, 25, 0, 0, 0));

        LocalDateTime sinceDate = LocalDateTime.of(2022, 6, 20, 0, 0, 0);
        LocalDateTime untilDate = LocalDateTime.of(2022, 6, 28, 0, 0, 0);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
        String formatDateTime = rocketStrike1.getStrikeDate().format(formatter);

        when(rocketStrikeService.getFilteredRocketStrikes(sinceDate, untilDate, List.of(region1),
                SORT_BY_REGION, ASC_ORDER)).thenReturn(List.of(rocketStrike1));

        this.mockMvc.perform(post("/mvc/getFilteredRocketStrikes")
                        .param("sinceDate", sinceDate.toString())
                        .param("untilDate", untilDate.toString())
                        .param("checkedRegionsId", String.valueOf(region1.getId()))
                        .param("sortField", SORT_BY_REGION)
                        .param("sortDir", ASC_ORDER))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8)))
                .andExpect(content().string(containsString(formatDateTime)))
                .andExpect(content().string(containsString(region1.getRegionalCentre())));
    }


    @Test
    public void viewFilteredRocketStrikesRESTTest() throws Exception {

        RocketStrikeDto rocketStrikeDto = new RocketStrikeDto();

        LocalDateTime sinceDate = LocalDateTime.of(2022, 6, 20, 0, 0, 0);
        LocalDateTime untilDate = LocalDateTime.of(2022, 6, 28, 0, 0, 0);
        List<Long> checkedRegionsId = List.of(1L);

        rocketStrikeDto.setSinceDate(LocalDateTime.parse(sinceDate.format(STRIKE_DATE_FORMATTER)));
        rocketStrikeDto.setUntilDate(LocalDateTime.parse(untilDate.format(STRIKE_DATE_FORMATTER)));
        rocketStrikeDto.setCheckedRegionsId(checkedRegionsId);
        rocketStrikeDto.setSortField(SORT_BY_REGION);
        rocketStrikeDto.setSortDir(ASC_ORDER);

        Region region1 = new Region(1, "Львівська область", "Львівщина", "Lviv", "Lviv");
        RocketStrike rocketStrike1 = new RocketStrike(1, region1, LocalDateTime.of(2022, 6, 25, 0, 0, 0));

//        ObjectMapper mapper =  new ObjectMapper();
//        String requestJson = mapper.writeValueAsString(rocketStrikeDto);
        String requestJson = "{\n" +
                "    \"sinceDate\":\"" + sinceDate + "\",\n" +
                "    \"untilDate\":\"" + untilDate + "\",\n" +
                "    \"sortField\":\"" + SORT_BY_REGION + "\",\n" +
                "    \"sortDir\":\"" + ASC_ORDER + "\",\n" +
                "    \"checkedRegionsId\": [" + region1.getId() + "]\n" +
                "}";

        when(rocketStrikeService.getFilteredRocketStrikes(rocketStrikeDto)).thenReturn(List.of(rocketStrike1));

        mockMvc.perform(MockMvcRequestBuilders.post("/rest/getFilteredRocketStrikes")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}