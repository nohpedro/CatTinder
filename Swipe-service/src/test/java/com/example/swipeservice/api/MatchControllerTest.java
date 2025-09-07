package com.example.swipeservice.api;

import com.example.swipeservice.Aplication.SwipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MatchController.class)
class MatchControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SwipeService service;

    @Test
    void listarMatchesDevuelveLista() throws Exception {
        when(service.listarMatchesDe("A")).thenReturn(List.of("B","C"));

        mvc.perform(get("/api/v1/matches/A"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.userId").value("A"))
           .andExpect(jsonPath("$.matches[0]").value("B"))
           .andExpect(jsonPath("$.matches[1]").value("C"));
    }
}
