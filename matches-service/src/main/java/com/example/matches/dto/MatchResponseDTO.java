package com.example.matches.dto;

import com.example.matches.model.Match;

import java.time.LocalDateTime;

// DTO de salida para representar un match
public class MatchResponseDTO {

    private Long matchId;
    private Long user1Id;
    private Long user2Id;
    private LocalDateTime matchedAt;
    private boolean active;  // ahora usamos booleano

    public MatchResponseDTO() {}

    public MatchResponseDTO(Long matchId,
                            Long user1Id,
                            Long user2Id,
                            LocalDateTime matchedAt,
                            boolean active) {
        this.matchId = matchId;
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.matchedAt = matchedAt;
        this.active = active;
    }

    // Mapper estático desde la entidad Match
    public static MatchResponseDTO fromEntity(Match match) {
        return new MatchResponseDTO(
                match.getId(),
                match.getUser1Id(),
                match.getUser2Id(),
                match.getMatchedAt(),
                match.isActive()  // usamos el booleano
        );
    }

    // Getters
    public Long getMatchId() { return matchId; }
    public Long getUser1Id() { return user1Id; }
    public Long getUser2Id() { return user2Id; }
    public LocalDateTime getMatchedAt() { return matchedAt; }
    public boolean isActive() { return active; }
}
