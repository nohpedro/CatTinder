package com.example.matches.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "matches",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user1_id", "user2_id"})
        },
        indexes = {
                @Index(name = "idx_user1", columnList = "user1_id"),
                @Index(name = "idx_user2", columnList = "user2_id"),
                @Index(name = "idx_active", columnList = "active")
        }
)
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user1_id", nullable = false)
    private Long user1Id;

    @Column(name = "user2_id", nullable = false)
    private Long user2Id;

    @Column(nullable = false)
    private LocalDateTime matchedAt;

    @Column(nullable = false)
    private boolean active;

    protected Match() {}

    public Match(Long userAId, Long userBId) {
        if (userAId == null || userBId == null) {
            throw new IllegalArgumentException("Los IDs de usuario no pueden ser null");
        }

        if (userAId.equals(userBId)) {
            throw new IllegalArgumentException("Un usuario no puede hacer match consigo mismo");
        }

        if (userAId < userBId) {
            this.user1Id = userAId;
            this.user2Id = userBId;
        } else {
            this.user1Id = userBId;
            this.user2Id = userAId;
        }

        this.matchedAt = LocalDateTime.now();
        this.active = true; // activo por defecto al crear
    }

    // -----------------
    // Dominio
    // -----------------
    public void cancel() {
        this.active = false;
    }

    public boolean isActive() {
        return this.active;
    }

    // -----------------
    // Getters
    // -----------------
    public Long getId() { return id; }
    public Long getUser1Id() { return user1Id; }
    public Long getUser2Id() { return user2Id; }
    public LocalDateTime getMatchedAt() { return matchedAt; }
    public boolean isActiveFlag() { return active; }
}
