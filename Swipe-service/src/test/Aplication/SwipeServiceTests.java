package com.example.swipeservice.Aplication;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SwipeServiceTests {

    @Test
    void generaMatchCuandoAmbosHacenLike() {
        var svc = new SwipeService();
        assertNull(svc.procesarSwipeYQuizasMatch("A","B","like"));
        assertNotNull(svc.procesarSwipeYQuizasMatch("B","A","like"));
    }

    @Test
    void noGeneraMatchSiUnoDiceNoLike() {
        var svc = new SwipeService();
        assertNull(svc.procesarSwipeYQuizasMatch("A","B","nolike"));
        assertNull(svc.procesarSwipeYQuizasMatch("B","A","like"));
    }
}
