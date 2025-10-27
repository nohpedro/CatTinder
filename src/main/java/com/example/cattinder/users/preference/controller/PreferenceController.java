package com.example.cattinder.users.preference.controller;

import com.example.cattinder.users.preference.dto.PreferenceDTO;
import com.example.cattinder.users.preference.model.Preference;
import com.example.cattinder.users.preference.service.PreferenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/preferences")
public class PreferenceController {

    private final PreferenceService preferenceService;

    public PreferenceController(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    @Operation(summary = "Crear una nueva preferencia para un usuario")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Preferencia creada exitosamente",
                    content = @Content(schema = @Schema(implementation = Preference.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos",
                    content = @Content(schema = @Schema(implementation = PreferenceDTO.class))
            )
    })
    @PostMapping
    public ResponseEntity<Preference> createPreference(@Valid @RequestBody PreferenceDTO dto) {
        Preference preference = preferenceService.createPreference(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(preference);
    }

    @Operation(summary = "Obtener todas las preferencias de un usuario")
    @ApiResponse(
            responseCode = "200",
            description = "Preferencias devueltas exitosamente",
            content = @Content(schema = @Schema(implementation = Preference.class))
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Preference>> getPreferencesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(preferenceService.getPreferencesByUserId(userId));
    }

    // NUEVO: ejemplo de consulta nativa con filtro por availability
    @Operation(summary = "Obtener preferencias de un usuario filtradas por disponibilidad (usa consulta nativa SQL)")
    @ApiResponse(
            responseCode = "200",
            description = "Preferencias filtradas devueltas exitosamente",
            content = @Content(schema = @Schema(implementation = Preference.class))
    )
    @GetMapping("/user/{userId}/filter")
    public ResponseEntity<List<Preference>> getPreferencesByUserAndAvailability(
            @PathVariable Long userId,
            @RequestParam String availability
    ) {
        List<Preference> prefs = preferenceService.getPreferencesByUserIdAndAvailability(userId, availability);
        return ResponseEntity.ok(prefs);
    }

    @Operation(summary = "Actualizar una preferencia existente")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Preferencia actualizada",
                    content = @Content(schema = @Schema(implementation = Preference.class))
            ),
            @ApiResponse(responseCode = "404", description = "Preferencia no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Preference> updatePreference(
            @PathVariable Long id,
            @Valid @RequestBody PreferenceDTO dto
    ) {
        return ResponseEntity.ok(preferenceService.updatePreference(id, dto));
    }

    @Operation(summary = "Eliminar una preferencia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Preferencia eliminada"),
            @ApiResponse(responseCode = "404", description = "Preferencia no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePreference(@PathVariable Long id) {
        preferenceService.deletePreference(id);
        return ResponseEntity.noContent().build();
    }
}
