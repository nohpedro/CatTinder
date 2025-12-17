package com.example.matches.controller;

import com.example.matches.dto.MatchResponseDTO;
import com.example.matches.model.Match;
import com.example.matches.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/matches")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @Operation(summary = "Crear un nuevo match (interno cuando hay like mutuo)")
    @PostMapping
    public ResponseEntity<MatchResponseDTO> createMatch(
            @RequestParam Long userAId,
            @RequestParam Long userBId
    ) {
        Match match = matchService.createMatch(userAId, userBId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MatchResponseDTO.fromEntity(match));
    }

    @Operation(summary = "Obtener todos los matches activos de un usuario")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MatchResponseDTO>> getMatchesForUser(@PathVariable Long userId) {
        List<MatchResponseDTO> list = matchService.getMatchesForUser(userId)
                .stream()
                .map(MatchResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Cancelar un match entre dos usuarios")
    @DeleteMapping
    public ResponseEntity<Void> cancelMatch(
            @RequestParam Long userAId,
            @RequestParam Long userBId
    ) {
        matchService.cancelMatch(userAId, userBId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Verificar si existe un match activo entre dos usuarios")
    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsMatch(
            @RequestParam Long userAId,
            @RequestParam Long userBId
    ) {
        boolean exists = matchService.existsMatchBetween(userAId, userBId);
        return ResponseEntity.ok(exists);
    }

    @Operation(summary = "Obtener un match específico activo entre dos usuarios")
    @GetMapping("/between")
    public ResponseEntity<MatchResponseDTO> getMatchBetween(
            @RequestParam Long userAId,
            @RequestParam Long userBId
    ) {
        Match match = matchService.getMatchBetween(userAId, userBId)
                .orElse(null);
        if (match == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(MatchResponseDTO.fromEntity(match));
    }
}
