package com.example.swipeservice.api;

import com.example.swipeservice.dto.MatchDto;
import com.example.swipeservice.Aplication.SwipeService;
import com.example.swipeservice.dto.SwipeDto;
import com.example.swipeservice.excepcion.ExcepcionNoEncontrado;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/swipes")
public class SwipeController {

    private final SwipeService servicio;

    public SwipeController(SwipeService servicio) {
        this.servicio = servicio;
    }

    @PostMapping(value = "/{actorId}/{targetId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> hacerSwipe(@PathVariable String actorId,
                                        @PathVariable String targetId,
                                        @RequestBody SwipeDto body) {

        String matchId = servicio.procesarSwipeYQuizasMatch(actorId, targetId, body.getDir());

        if (matchId != null) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("mensaje", "match", "matchId", matchId));
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("mensaje", "Swipe");
        resp.put("matchId", null);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    /** Devuelve un match existente entre dos usuarios. */
    @GetMapping("/matches/{uidA}/{uidB}")
    public ResponseEntity<MatchDto> obtenerMatch(@PathVariable String uidA, @PathVariable String uidB) {
        MatchDto m = servicio.obtenerMatch(uidA, uidB);
        if (m == null) throw new ExcepcionNoEncontrado("No existe match entre " + uidA + " y " + uidB);
        return ResponseEntity.ok(m);
    }
}
