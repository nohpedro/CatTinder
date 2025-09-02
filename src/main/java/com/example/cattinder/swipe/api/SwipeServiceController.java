package com.example.cattinder.swipe.api;

import com.example.cattinder.swipe.aplication.SwipeServiceApplication;
import com.example.cattinder.swipe.dto.MatchDTO;
import com.example.cattinder.swipe.dto.SwipeRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// Cuidado con el Request Boduy de esta importacion choca con el OPEN api



import java.util.HashMap;
import java.util.Map;

// imports OpenAPI
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
// import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

// Validaciones import XD
import jakarta.validation.Valid;




@Tag(name = "Swipes", description = "Operaciones de swipe y match")
@RestController
@RequestMapping("/api/swipes")
public class SwipeServiceController {

    private final SwipeServiceApplication app;

    public SwipeServiceController(SwipeServiceApplication app) {
        this.app = app;
    }
    @Operation(
        summary = "Registrar swipe",
        description = "Registra like|nope|superlike de actorId hacia targetId. "
                    + "Si existe reciprocidad positiva, se crea un match (estado=active)."
        )
        @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Swipe registrado correctamente",
            content = @Content(schema = @Schema(implementation = java.util.Map.class))),
        @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno")
        })
    /** POST /api/swipes/{actorId}/{targetId}  body: {"dir":"like|nope|superlike"} */
    @PostMapping("/{actorId}/{targetId}")
    public ResponseEntity<Map<String, Object>> swipe(
                                                     @Parameter(description = "Usuario que hace el swipe", required = true)
                                                     @PathVariable String actorId,
                                                     @Parameter(description = "Usuario objetivo del swipe", required = true)
                                                     @PathVariable String targetId,
                                                     
                                                     @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                        required = false,
                                                        description = "Dirección del swipe (like|nope|superlike)",
                                                        content = @Content(schema = @Schema(implementation = SwipeRequestDTO.class))
                                                     )
                                                     @org.springframework.web.bind.annotation.RequestBody(required = false)
                                                        /* @Valid */ com.example.cattinder.swipe.dto.SwipeRequestDTO body,  // descomenta @Valid si activas validación
                                                     @Parameter(description = "Dirección alternativa vía query (like|nope|superlike)")
                                                     @RequestParam(value = "dir", required = false) String dirParam) throws Exception {
        String dir = (body != null && body.getDir() != null) ? body.getDir()
                : (dirParam != null ? dirParam : "");

        dir = dir.toLowerCase();

        if (!(dir.equals("like") || dir.equals("nope") || dir.equals("superlike"))) {
            return ResponseEntity.badRequest()
                    .body(Map.of("ok", false, "error", "dir debe ser like|nope|superlike"));
        }

        String matchId = app.handleSwipeAndMaybeMatch(actorId, targetId, dir);
        boolean matched = (matchId != null);

        // Map.of no acepta null: construimos a mano
        var resp = new HashMap<String, Object>();
        resp.put("ok", true);
        resp.put("dir", dir);
        resp.put("matched", matched);
        if (matched) resp.put("matchId", matchId);

        return ResponseEntity.ok(resp);
    }

    /** GET /api/swipes/match/{uidA}/{uidB} */
    @Operation(
        summary = "Obtener match",
        description = "Devuelve el MatchDTO si existe match entre uidA y uidB."
        )
        @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = com.example.cattinder.swipe.dto.MatchDTO.class))),
        })
    @GetMapping("/match/{uidA}/{uidB}")
    
    public ResponseEntity<?> getMatch(
        @Parameter(description = "Usuario A", required = true) @PathVariable String uidA,
        @Parameter(description = "Usuario B", required = true) @PathVariable String uidB
    ) throws Exception {
        MatchDTO dto = app.getMatch(uidA, uidB);
        if (dto == null) return ResponseEntity.ok(Map.of()); // no existe
        return ResponseEntity.ok(dto);
    }
}
