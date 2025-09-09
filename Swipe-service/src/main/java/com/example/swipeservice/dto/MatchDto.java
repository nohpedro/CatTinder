package com.example.swipeservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(
        name = "Match",
        description = "Información de un match entre dos usuarios",
        example = "{\"usuarioA\":\"hola\",\"usuarioB\":\"adios\",\"estado\":\"activo\",\"fechaCreacion\":\"2025-09-08T12:34:56\"}"
)
@JsonPropertyOrder({"usuarioA","usuarioB","estado","fechaCreacion"})
public class MatchDto {

    @Schema(description = "Usuario A (normalizado)", example = "hola")
    @JsonProperty("usuarioA")
    @NotBlank(message = "usuarioA no puede estar vacío")
    @Size(min = 3, max = 50, message = "usuarioA debe tener entre 3 y 50 caracteres")
    private String UsuarioA;

    @Schema(description = "Usuario B (normalizado)", example = "adios")
    @JsonProperty("usuarioB")
    @NotBlank(message = "usuarioB no puede estar vacío")
    @Size(min = 3, max = 50, message = "usuarioB debe tener entre 3 y 50 caracteres")
    private String UsuarioB;

    @Schema(description = "Estado del match", example = "activo", allowableValues = {"activo","inactivo"})
    @JsonProperty("estado")
    @NotBlank(message = "estado es obligatorio")
    @Pattern(regexp = "activo|inactivo", message = "estado debe ser 'activo' o 'inactivo'")
    private String Estado;

    @Schema(description = "Fecha de creación ISO-8601", example = "2025-09-08T12:34:56", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty("fechaCreacion")
    private String FechaCreacion;

    public MatchDto() {}

    public MatchDto(String UsuarioA, String UsuarioB, String Estado) {
        this.UsuarioA = UsuarioA;
        this.UsuarioB = UsuarioB;
        this.Estado = Estado;
        this.FechaCreacion = java.time.LocalDateTime.now().toString();
    }

    public String getUsuarioA() { return this.UsuarioA; }
    public void setUsuarioA(String UsuarioA) { this.UsuarioA = UsuarioA; }

    public String getUsuarioB() { return this.UsuarioB; }
    public void setUsuarioB(String UsuarioB) { this.UsuarioB = UsuarioB; }

    public String getEstado() { return this.Estado; }
    public void setEstado(String Estado) { this.Estado = Estado; }

    public String getFechaCreacion() { return this.FechaCreacion; }
    public void setFechaCreacion(String fecha) { this.FechaCreacion = fecha; } // opcional
}
