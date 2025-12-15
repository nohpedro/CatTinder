package com.example.swipeservice.dominio.entidad;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
        name = "matches",
        uniqueConstraints = @UniqueConstraint(name = "uk_match_usuarios", columnNames = {"usuarioA", "usuarioB"}),
        indexes = {
                @Index(name = "idx_match_usuarioA", columnList = "usuarioA"),
                @Index(name = "idx_match_usuarioB", columnList = "usuarioB")
        }
)
public class MatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // guardamos ordenados (usuarioA <= usuarioB) para unicidad
    @Column(nullable=false, length=120)
    private String usuarioA;

    @Column(nullable=false, length=120)
    private String usuarioB;

    @Column(nullable=false, length=16)
    private String estado; // "activo"

    @Column(nullable=false)
    private Instant matchedAt = Instant.now();

    public MatchEntity() { }

    public MatchEntity(String usuarioA, String usuarioB, String estado) {
        this.usuarioA = usuarioA;
        this.usuarioB = usuarioB;
        this.estado = estado;
    }

    public Long getId() { return id; }
    public String getUsuarioA() { return usuarioA; }
    public void setUsuarioA(String usuarioA) { this.usuarioA = usuarioA; }
    public String getUsuarioB() { return usuarioB; }
    public void setUsuarioB(String usuarioB) { this.usuarioB = usuarioB; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Instant getMatchedAt() { return matchedAt; }
    public void setMatchedAt(Instant matchedAt) { this.matchedAt = matchedAt; }
}
