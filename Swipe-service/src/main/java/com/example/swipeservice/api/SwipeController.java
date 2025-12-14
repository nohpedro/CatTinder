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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    // -------------------- SWIPE PRINCIPAL --------------------

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

    // -------------------- MATCH EXISTENTE --------------------

    @Operation(
            summary = "Obtener match entre dos usuarios",
            description = "Devuelve la información del match entre dos usuarios si existe.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Match encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MatchDto.class))),
                    @ApiResponse(responseCode = "404", description = "No existe match entre los usuarios")
            }
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
    // -------------------- NATIVO: EXISTE MATCH --------------------

    @Operation(
            summary = "Verificar si existe un match entre dos usuarios",
            description = "Consulta nativa que retorna true/false si existe un match entre los usuarios especificados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Consulta exitosa",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"u1\":\"alex\",\"u2\":\"camila\",\"existe\":true}")))
            }
    )
    @GetMapping("/matches/existe")
    public Map<String, Object> existeMatch(
            @Parameter(description = "Usuario 1", example = "alex") @RequestParam String u1,
            @Parameter(description = "Usuario 2", example = "camila") @RequestParam String u2) {
        return Map.of("u1", u1, "u2", u2, "existe", servicio.existeMatch(u1, u2));
    }

    // -------------------- PING (Health interno) --------------------

    @Operation(
            summary = "Ping de salud del servicio",
            description = "Devuelve un simple JSON indicando que el servicio está operativo.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Servicio activo",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"status\": \"UP\", \"source\": \"swipe-service\"}")))
            }
    )
    @GetMapping("/ping")
    public Map<String, Object> ping() {
        return Map.of("status", "UP", "source", "swipe-service");
    }

    // -------------------- OPCIONAL: Diagnóstico de instancias Eureka --------------------

    @Operation(
            summary = "Diagnóstico: instancias registradas en Eureka",
            description = "Lista las instancias activas de 'swipe-service' detectadas por el cliente Eureka.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Instancias obtenidas correctamente")
            }
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
