package com.example.swipeservice;

import com.example.swipeservice.Aplication.SwipeService;
import com.example.swipeservice.dto.MatchDto;
import com.example.swipeservice.dominio.repositorio.MatchRepository;
import com.example.swipeservice.dominio.repositorio.SwipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SwipeServiceTest {

    @Mock
    private SwipeRepository swipeRepository;

    @Mock
    private MatchRepository matchRepository;

    private SwipeService swipeService;

    @BeforeEach
    void setUp() {
        swipeService = new SwipeService(swipeRepository, matchRepository);
    }

    @Test
    void testLuciaSwipeToPedro() {
        String matchId = swipeService.procesarSwipeYQuizasMatch("lucia", "pedro", "right");
        assertNull(matchId);
        MatchDto match = swipeService.obtenerMatch("lucia", "pedro");
        assertNull(match);
    }
}
