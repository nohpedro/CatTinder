package com.example.swipeservice.api;

import com.example.swipeservice.Aplication.SwipeService;
import com.example.swipeservice.dto.MatchDto;
import com.example.swipeservice.dto.SwipeDto;
import com.example.swipeservice.excepcion.ExcepcionNoEncontrado;
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

    @GetMapping("/matches/{uidA}/{uidB}")
    public ResponseEntity<MatchDto> obtenerMatch(@PathVariable String uidA, @PathVariable String uidB) {
        MatchDto m = servicio.obtenerMatch(uidA, uidB);
        if (m == null) throw new ExcepcionNoEncontrado("No existe match entre " + uidA + " y " + uidB);
        return ResponseEntity.ok(m);
    }

    /** PING local del servicio (sin discovery), útil para el test. */
    @GetMapping("/ping")
    public Map<String, Object> ping() {
        return Map.of("status", "UP", "source", "swipe-service");
    }

    /** Test de descubrimiento dinámico usando el NOMBRE LÓGICO en Eureka. */
    @GetMapping("/test-discovery")
    public ResponseEntity<Map<String, Object>> testDiscovery() {
        Map<String, Object> result = new HashMap<>();
        // Llamada a este MISMO servicio por nombre lógico (resuelto por Eureka + LoadBalancer)
        String url = "http://swipe-service/api/v1/swipes/ping";

        try {
            String response = restTemplate.getForObject(url, String.class);
            result.put("mensaje", "Descubrimiento dinámico OK");
            result.put("servicio", "swipe-service");
            result.put("respuesta", response);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("mensaje", "Error en descubrimiento dinámico");
            result.put("detalle", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /** Diagnóstico: ¿qué instancias ve este cliente para 'swipe-service' en Eureka? */
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
