package com.example.swipeservice.api;

import com.example.swipeservice.dto.MatchDto;



import com.example.swipeservice.Aplication.SwipeService;
import com.example.swipeservice.dto.SwipeDto;
import com.example.swipeservice.dto.SwipeResponse;
import com.example.swipeservice.excepcion.ExcepcionNoEncontrado;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/swipes")
public class SwipeController {

    private final SwipeService servicio;

    public SwipeController(SwipeService servicio) {
        this.servicio = servicio;
    }

    /** Registra un swipe. Si hay reciprocidad positiva, devuelve matchId. */
    /*@PostMapping("/{actorId}/{targetId}")
    public ResponseEntity<?> hacerSwipe(@PathVariable String actorId,
                                        @PathVariable String targetId,
                                        @Valid @RequestBody SwipeDto body) {
        String matchId = servicio.procesarSwipeYQuizasMatch(actorId, targetId, body.getDir());
        if (matchId != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    java.util.Map.of("mensaje", "¡Hay match!", "matchId", matchId));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(
                java.util.Map.of("mensaje", "Swipe registrado", "matchId", null));
    }*/
    @PostMapping("/{actorId}/{targetId}")
        public ResponseEntity<SwipeResponse> hacerSwipe(@PathVariable String actorId,
                                                        @PathVariable String targetId,
                                                        @Valid @RequestBody SwipeDto body) {
            String matchId = servicio.procesarSwipeYQuizasMatch(actorId, targetId, body.getDir());
            if (matchId != null) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new SwipeResponse("¡Hay match!", matchId));
            }
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new SwipeResponse("Swipe registrado", null));
        }


    /** Devuelve un match existente entre dos usuarios. */
    @GetMapping("/matches/{uidA}/{uidB}")
    public ResponseEntity<MatchDto> obtenerMatch(@PathVariable String uidA, @PathVariable String uidB) {
        MatchDto m = servicio.obtenerMatch(uidA, uidB);
        if (m == null) throw new ExcepcionNoEncontrado("No existe match entre " + uidA + " y " + uidB);
        return ResponseEntity.ok(m);
    }
}