package com.example.intereses.controller;

import com.example.intereses.dto.InterestDTO;
import com.example.intereses.model.Interest;
import com.example.intereses.service.InterestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/interests")
@RequiredArgsConstructor
@Tag(name = "Intereses", description = "API para gestión de intereses")
public class InterestController {
    
    private final InterestService interestService;
    
    @PostMapping
    @Operation(summary = "Crear un nuevo interés")
    public ResponseEntity<Interest> createInterest(@Valid @RequestBody InterestDTO interestDTO) {
        Interest interest = interestService.createInterest(interestDTO);
        return new ResponseEntity<>(interest, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un interés por ID")
    public ResponseEntity<Interest> getInterestById(@PathVariable Long id) {
        Interest interest = interestService.getInterestById(id);
        return ResponseEntity.ok(interest);
    }
    
    @GetMapping
    @Operation(summary = "Obtener todos los intereses")
    public ResponseEntity<List<Interest>> getAllInterests() {
        List<Interest> interests = interestService.getAllInterests();
        return ResponseEntity.ok(interests);
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Obtener intereses por ID de usuario")
    public ResponseEntity<List<Interest>> getInterestsByUserId(@PathVariable Long userId) {
        List<Interest> interests = interestService.getInterestsByUserId(userId);
        return ResponseEntity.ok(interests);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Buscar intereses por nombre")
    public ResponseEntity<List<Interest>> searchInterests(@RequestParam String nombre) {
        List<Interest> interests = interestService.searchInterestsByName(nombre);
        return ResponseEntity.ok(interests);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un interés")
    public ResponseEntity<Interest> updateInterest(@PathVariable Long id, 
                                                   @Valid @RequestBody InterestDTO interestDTO) {
        Interest interest = interestService.updateInterest(id, interestDTO);
        return ResponseEntity.ok(interest);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un interés")
    public ResponseEntity<Void> deleteInterest(@PathVariable Long id) {
        interestService.deleteInterest(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/match/{userId1}/{userId2}")
    @Operation(summary = "Verificar si dos usuarios tienen intereses en común")
    public ResponseEntity<Boolean> tienenInteresesEnComun(@PathVariable Long userId1, 
                                                           @PathVariable Long userId2) {
        boolean tienenMatch = interestService.tienenInteresesEnComun(userId1, userId2);
        return ResponseEntity.ok(tienenMatch);
    }
    
    @GetMapping("/compartidos/{userId1}/{userId2}")
    @Operation(summary = "Obtener intereses compartidos entre dos usuarios")
    public ResponseEntity<List<Interest>> encontrarInteresesCompartidos(@PathVariable Long userId1, 
                                                                         @PathVariable Long userId2) {
        List<Interest> interesesCompartidos = interestService.encontrarInteresesCompartidos(userId1, userId2);
        return ResponseEntity.ok(interesesCompartidos);
    }
}

