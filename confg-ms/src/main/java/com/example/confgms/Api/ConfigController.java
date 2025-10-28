package com.example.confgms.Api;

import com.example.confgms.Aplication.configService;
import com.example.confgms.dto.configDto;
import com.example.confgms.excepcion.Error.ExcepcionNoEncontrado;
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

import java.util.List;
import java.util.Map;

@Tag(name = "Config", description = "Operaciones principales del Config Service")
@RestController
@RequestMapping("/api/v1/config")
public class ConfigController {

    private final configService servicio;
    private final DiscoveryClient discoveryClient;

    public ConfigController(configService servicio, DiscoveryClient discoveryClient) {
        this.servicio = servicio;
        this.discoveryClient = discoveryClient;
    }

    // -------------------- CREAR CONFIG --------------------

    @Operation(
            summary = "Crear configuración de un usuario",
            description = "Crea una configuración nueva para el usuario indicado. Si ya existe, responde 400.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Configuración creada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = configDto.class))),
                    @ApiResponse(responseCode = "400", description = "Solicitud incorrecta (por ejemplo, userid vacío o ya existente)"),
            }
    )
    @PostMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<configDto> crear(
            @Parameter(description = "ID del usuario", example = "mariel") @PathVariable String userId,
            @Valid @RequestBody configDto body) {

        body.setUserid(userId); // coherencia path/body
        configDto creado = servicio.crear(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // -------------------- OBTENER CONFIG --------------------

    @Operation(
            summary = "Obtener configuración por usuario",
            description = "Devuelve la configuración del usuario si existe.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Configuración encontrada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = configDto.class))),
                    @ApiResponse(responseCode = "404", description = "No existe configuración para el usuario")
            }
    )
    @GetMapping("/{userId}")
    public ResponseEntity<configDto> obtener(
            @Parameter(description = "ID del usuario", example = "mariel") @PathVariable String userId) {

        configDto dto = servicio.obtener(userId);
        if (dto == null) {
            throw new ExcepcionNoEncontrado("No existe configuración para: " + userId);
        }
        return ResponseEntity.ok(dto);
    }

    // -------------------- REEMPLAZAR / UPSERT (PUT) --------------------

    @Operation(
            summary = "Reemplazar o crear configuración (PUT)",
            description = "Reemplaza completamente la configuración del usuario; si no existe, la crea. Campos nulos se consideran false.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Configuración reemplazada/creada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = configDto.class))),
                    @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
            }
    )
    @PutMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<configDto> upsert(
            @Parameter(description = "ID del usuario", example = "mariel") @PathVariable String userId,
            @Valid @RequestBody configDto body) {

        body.setUserid(userId); // coherencia path/body
        return ResponseEntity.ok(servicio.upsert(userId, body));
    }

    // -------------------- PATCH PARCIAL --------------------

    @Operation(
            summary = "Actualización parcial de configuración (PATCH)",
            description = "Actualiza solo los campos no nulos del cuerpo (Boolean nulos no se tocan).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Configuración actualizada parcialmente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = configDto.class))),
                    @ApiResponse(responseCode = "404", description = "No existe configuración para el usuario")
            }
    )
    @PatchMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<configDto> patch(
            @Parameter(description = "ID del usuario", example = "mariel") @PathVariable String userId,
            @RequestBody configDto partial) {

        return ResponseEntity.ok(servicio.patch(userId, partial));
    }

    // -------------------- ELIMINAR --------------------

    @Operation(
            summary = "Eliminar configuración",
            description = "Elimina la configuración del usuario si existe.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Eliminado"),
                    @ApiResponse(responseCode = "404", description = "No existe configuración para el usuario")
            }
    )
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del usuario", example = "mariel") @PathVariable String userId) {

        servicio.eliminar(userId);
        return ResponseEntity.noContent().build();
    }

    // -------------------- PING (Health interno) --------------------

    @Operation(
            summary = "Ping de salud del servicio",
            description = "Devuelve un simple JSON indicando que el servicio está operativo.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Servicio activo",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"status\": \"UP\", \"source\": \"config-service\"}")))
            }
    )
    @GetMapping("/ping")
    public Map<String, Object> ping() {
        return Map.of("status", "UP", "source", "config-service");
    }

    // -------------------- Diagnóstico de instancias Eureka --------------------

    @Operation(
            summary = "Diagnóstico: instancias registradas en Eureka",
            description = "Lista las instancias activas de 'config-service' detectadas por el cliente Eureka.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Instancias obtenidas correctamente")
            }
    )
    @GetMapping("/_instances")
    public Map<String, Object> instances() {
        List<ServiceInstance> list = discoveryClient.getInstances("config-service");
        return Map.of(
                "serviceId", "config-service",
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
