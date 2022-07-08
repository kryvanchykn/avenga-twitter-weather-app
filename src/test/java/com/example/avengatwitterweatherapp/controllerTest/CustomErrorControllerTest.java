package com.example.avengatwitterweatherapp.controllerTest;

import com.example.avengatwitterweatherapp.controller.CustomErrorController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.RequestDispatcher;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomErrorController.class)
public class CustomErrorControllerTest {
    private static final String DEFAULT_ERROR_MESSAGE = "Something went wrong!";
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void badRequestErrorTest() throws Exception {
        String errorMessage = "Maybe, you forgot to select regions before making request";
        this.mockMvc.perform(get("/mvc/error").requestAttr(RequestDispatcher.ERROR_STATUS_CODE, "400"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(content().contentType(new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8)))
                .andExpect(content().string(containsString(DEFAULT_ERROR_MESSAGE)))
                .andExpect(content().string(containsString(errorMessage)));
    }

    @Test
    public void notFoundErrorTest() throws Exception {
        String errorMessage = "Maybe, you want to see a forecast for the date which was more than week ago";
        this.mockMvc.perform(get("/mvc/error").requestAttr(RequestDispatcher.ERROR_STATUS_CODE, "404"))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(content().contentType(new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8)))
                .andExpect(content().string(containsString(DEFAULT_ERROR_MESSAGE)))
                .andExpect(content().string(containsString(errorMessage)));
    }

    @Test
    public void notAcceptableErrorTest() throws Exception {
        String errorMessage = "Maybe, since date is after until date";
        this.mockMvc.perform(get("/mvc/error").requestAttr(RequestDispatcher.ERROR_STATUS_CODE, "406"))
                .andDo(print()).andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8)))
                .andExpect(content().string(containsString(DEFAULT_ERROR_MESSAGE)))
                .andExpect(content().string(containsString(errorMessage)));
    }

    @Test
    public void defaultErrorTest() throws Exception {
        this.mockMvc.perform(get("/mvc/error"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(content().contentType(new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8)))
                .andExpect(content().string(containsString(DEFAULT_ERROR_MESSAGE)));
    }
}
