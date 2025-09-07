package com.example.swipeservice.api;

import com.example.swipeservice.Aplication.SwipeService;
import com.example.swipeservice.dto.SwipeDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SwipeController.class)
class SwipeControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SwipeService service;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void hacerSwipeDevuelveSwipeRegistrado() throws Exception {
        SwipeDto dto = new SwipeDto();
        dto.setDir("like");

        // simular que NO hay match
        when(service.procesarSwipeYQuizasMatch(eq("A"), eq("B"), any()))
            .thenReturn(null);

        mvc.perform(post("/api/v1/swipes/A/B")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.mensaje").value("Swipe registrado"))
            .andExpect(jsonPath("$.matchId").doesNotExist());
    }

    @Test
    void hacerSwipeDevuelveHayMatch() throws Exception {
        SwipeDto dto = new SwipeDto();
        dto.setDir("like");

        // simular que hay match
        when(service.procesarSwipeYQuizasMatch(eq("A"), eq("B"), any()))
            .thenReturn("match123");

        mvc.perform(post("/api/v1/swipes/A/B")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.mensaje").value("¡Hay match!"))
            .andExpect(jsonPath("$.matchId").value("match123"));
    }
}
