package com.example.swipeservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "Match",
        description = "Información de un match entre dos usuarios",
        example = "{\"usuarioA\":\"hola\",\"usuarioB\":\"adios\",\"estado\":\"activo\",\"fechaCreacion\":\"2025-09-08T12:34:56\"}"
)
@JsonPropertyOrder({"usuarioA","usuarioB","estado","fechaCreacion"})
public class MatchDto {

    @Schema(description = "Usuario A (normalizado)", example = "hola")
    @JsonProperty("usuarioA")
    private String UsuarioA;

    @Schema(description = "Usuario B (normalizado)", example = "adios")
    @JsonProperty("usuarioB")
    private String UsuarioB;

    @Schema(description = "Estado del match", example = "activo", allowableValues = {"activo","inactivo"})
    @JsonProperty("estado")
    private String Estado;

    @Schema(description = "Fecha de creación ISO-8601", example = "2025-09-08T12:34:56", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty("fechaCreacion")
    private String FechaCreacion;

    // Constructor vacío requerido por Jackson / springdoc
    public MatchDto() {}

    public MatchDto(String UsuarioA,String UsuarioB,String Estado) {
        this.UsuarioA = UsuarioA;
        this.UsuarioB = UsuarioB;
        this.Estado = Estado;
        this.FechaCreacion = java.time.LocalDateTime.now().toString();
    }

    public String getUsuarioA(){ return this.UsuarioA; }
    public void setUsuarioA(String UsuarioA){ this.UsuarioA = UsuarioA; }

    public String getUsuarioB(){ return this.UsuarioB; }
    public void setUsuarioB(String UsuarioB){ this.UsuarioB = UsuarioB; }

    public String getEstado(){ return this.Estado; }
    public void setEstado(String Estado){ this.Estado = Estado; }

    public String getFechaCreacion(){ return this.FechaCreacion; }
    public void setFechaCreacion(String fecha){ this.FechaCreacion = fecha; } // opcional
}
