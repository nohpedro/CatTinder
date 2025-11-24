package com.example.swipeservice.api;

import com.example.swipeservice.Aplication.SwipeService;
import com.example.swipeservice.dto.MatchDto;
import com.example.swipeservice.dto.SwipeDto;
import com.example.swipeservice.excepcion.ExcepcionNoEncontrado;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Swipes", description = "Operaciones principales del Swipe Service")
@RestController
@RequestMapping("/api/v1/swipes")
public class SwipeController {

    private final SwipeService servicio;
    private final RestTemplate restTemplate;
    private final DiscoveryClient discoveryClient;

    public SwipeController(SwipeService servicio, RestTemplate restTemplate, DiscoveryClient discoveryClient) {
        this.servicio = servicio;
        this.restTemplate = restTemplate;
        this.discoveryClient = discoveryClient;
    }

    @PostMapping(value = "/{actorId}/{targetId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Registrar un swipe",
            description = "Procesa un swipe entre dos usuarios y retorna información de match.",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
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

    @Operation(
            summary = "Obtener match entre dos usuarios",
            description = "Devuelve el match entre dos usuarios si existe.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Match encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MatchDto.class))),
                    @ApiResponse(responseCode = "404", description = "Match no encontrado")
            },
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @GetMapping("/matches/{uidA}/{uidB}")
    public ResponseEntity<MatchDto> obtenerMatch(
            @Parameter(description = "Usuario A", example = "alex") @PathVariable String uidA,
            @Parameter(description = "Usuario B", example = "camila") @PathVariable String uidB) {

        MatchDto m = servicio.obtenerMatch(uidA, uidB);
        if (m == null) {
            throw new ExcepcionNoEncontrado("No existe match entre " + uidA + " y " + uidB);
        }
        return ResponseEntity.ok(m);
    }

    @Operation(
            summary = "Verificar si existe un match",
            description = "Devuelve true o false dependiendo si existe un match entre u1 y u2.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Consulta exitosa")
            },
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @GetMapping("/matches/existe")
    public Map<String, Object> existeMatch(
            @Parameter(description = "Usuario 1", example = "alex") @RequestParam String u1,
            @Parameter(description = "Usuario 2", example = "camila") @RequestParam String u2) {

        return Map.of("u1", u1, "u2", u2, "existe", servicio.existeMatch(u1, u2));
    }

    @Operation(
            summary = "Ping del servicio",
            description = "Devuelve un JSON indicando el estado del servicio."
    )
    @GetMapping("/ping")
    public Map<String, Object> ping() {
        return Map.of("status", "UP", "source", "swipe-service");
    }

    @Operation(
            summary = "Diagnóstico de instancias Eureka",
            description = "Lista las instancias de swipe-service registradas en Eureka."
    )
    @GetMapping("/_instances")
    public Map<String, Object> instances() {
        List<ServiceInstance> list = discoveryClient.getInstances("swipe-service");
        return Map.of(
                "serviceId", "swipe-service",
                "count", list.size(),
                "instances", list.stream().map(si -> Map.of(
                        "host", si.getHost(),
                        "port", si.getPort(),
                        "uri", si.getUri().toString(),
                        "instanceId", si.getInstanceId()
                )).toList()
        );
    }
}
