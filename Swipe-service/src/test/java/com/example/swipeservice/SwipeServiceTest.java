package com.example.swipeservice;

import com.example.swipeservice.Aplication.SwipeService;
import com.example.swipeservice.dto.MatchDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SwipeServiceTest {

    private SwipeService swipeService;

    @BeforeEach
    void setUp() {
        swipeService = new SwipeService();
    }

    @Test
    void testLuciaSwipeToPedro() {
        String matchId = swipeService.procesarSwipeYQuizasMatch("lucia", "pedro", "right");
        assertNull(matchId);
        MatchDto match = swipeService.obtenerMatch("lucia", "pedro");
        assertNull(match);
    }
}
