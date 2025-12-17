package com.example.matches.controller;

import com.example.matches.dto.SwipeDTO;
import com.example.matches.exception.SwipeNotFoundException;
import com.example.matches.model.Swipe;
import com.example.matches.service.SwipeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/swipes")
public class SwipeController {

    private final SwipeService swipeService;

    public SwipeController(SwipeService swipeService) {
        this.swipeService = swipeService;
    }

    @Operation(summary = "Crear un nuevo swipe (like/dislike)")
    @PostMapping
    public ResponseEntity<Swipe> createSwipe(@Valid @RequestBody SwipeDTO swipeDTO) {
        Swipe swipe = swipeService.createSwipe(swipeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(swipe);
    }

    @Operation(summary = "Obtener todos los swipes realizados por un usuario")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Swipe>> getSwipesByUser(@PathVariable Long userId) {
        List<Swipe> swipes = swipeService.getSwipesByUser(userId);
        return ResponseEntity.ok(swipes);
    }

    @Operation(summary = "Verificar si existe un swipe entre dos usuarios")
    @GetMapping("/exists")
    public ResponseEntity<Swipe> checkSwipeExists(
            @RequestParam Long fromUserId,
            @RequestParam Long toUserId) {

        Swipe swipe = swipeService.getSwipe(fromUserId, toUserId)
                .orElseThrow(() -> new SwipeNotFoundException(
                        "Swipe no encontrado entre usuarios " + fromUserId + " y " + toUserId));

        return ResponseEntity.ok(swipe);
    }
}
