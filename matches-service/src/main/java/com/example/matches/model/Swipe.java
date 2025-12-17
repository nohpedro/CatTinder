package com.example.matches.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "swipes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"from_user_id", "to_user_id"})
        },
        indexes = {
                @Index(name = "idx_from_user", columnList = "from_user_id"),
                @Index(name = "idx_to_user", columnList = "to_user_id")
        }
)
public class Swipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usuario que hace el swipe
    @Column(name = "from_user_id", nullable = false)
    private Long fromUserId;

    // Usuario objetivo
    @Column(name = "to_user_id", nullable = false)
    private Long toUserId;

    // true = like, false = dislike
    @Column(nullable = false)
    private boolean liked;

    // Fecha del swipe
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // -----------------
    // Constructores
    // -----------------

    protected Swipe() {
        // requerido por JPA
    }

    public Swipe(Long fromUserId, Long toUserId, boolean liked) {
        if (fromUserId == null || toUserId == null) {
            throw new IllegalArgumentException("Los IDs de usuario no pueden ser null");
        }

        if (fromUserId.equals(toUserId)) {
            throw new IllegalArgumentException("Un usuario no puede hacerse swipe a sí mismo");
        }

        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.liked = liked;
        this.createdAt = LocalDateTime.now();
    }

    // -----------------
    // Getters
    // -----------------

    public Long getId() {
        return id;
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public boolean isLiked() {
        return liked;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Swipe{" +
                "id=" + id +
                ", fromUserId=" + fromUserId +
                ", toUserId=" + toUserId +
                ", liked=" + liked +
                ", createdAt=" + createdAt +
                '}';
    }
}
