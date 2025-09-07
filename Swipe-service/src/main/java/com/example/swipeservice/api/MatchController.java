package com.example.swipeservice.api;

import com.example.swipeservice.Aplication.SwipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.swipeservice.dto.MatchesResponse;

import java.util.List;
import java.util.Map;

@Tag(name = "Matches", description = "Consulta de matches del usuario")
@RestController
@RequestMapping("/api/v1/matches")
public class MatchController {

    private final SwipeService service;
    public MatchController(SwipeService service){ this.service = service; }

    @Operation(
        summary = "Listar matches",
        description = "Devuelve los IDs de usuarios con los que {userId} tiene match (demo en memoria)."
    )
    /*@GetMapping("/{userId}")
    public ResponseEntity<?> listar(@PathVariable String userId){
        List<String> matches = service.listarMatchesDe(userId);
        return ResponseEntity.ok(Map.of(
            "userId", userId,
            "matches", matches
        ));
    }*/
    @GetMapping("/{userId}")
    public ResponseEntity<MatchesResponse> listar(@PathVariable String userId){
        List<String> matches = service.listarMatchesDe(userId);
        return ResponseEntity.ok(new MatchesResponse(userId, matches));
    }

}
