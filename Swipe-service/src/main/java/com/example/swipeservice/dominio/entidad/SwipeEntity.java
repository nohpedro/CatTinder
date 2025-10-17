package com.example.swipeservice.dominio.entidad;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
        name = "swipes",
        uniqueConstraints = @UniqueConstraint(name = "uk_actor_objetivo", columnNames = {"actor", "objetivo"}),
        indexes = {
                @Index(name = "idx_swipe_actor", columnList = "actor"),
                @Index(name = "idx_swipe_objetivo", columnList = "objetivo")
        }
)
public class SwipeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=120)
    private String actor;

    @Column(nullable=false, length=120)
    private String objetivo;

    @Column(nullable=false, length=10)
    private String dir; // "right" / "left" / "like"

    @Column(nullable=false)
    private Instant createdAt = Instant.now();

    public SwipeEntity() { }

    public SwipeEntity(String actor, String objetivo, String dir) {
        this.actor = actor;
        this.objetivo = objetivo;
        this.dir = dir;
    }

    public Long getId() { return id; }
    public String getActor() { return actor; }
    public void setActor(String actor) { this.actor = actor; }
    public String getObjetivo() { return objetivo; }
    public void setObjetivo(String objetivo) { this.objetivo = objetivo; }
    public String getDir() { return dir; }
    public void setDir(String dir) { this.dir = dir; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
