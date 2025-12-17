package com.example.matches.service;

import com.example.matches.model.Match;

import java.util.List;
import java.util.Optional;

public interface MatchService {

    // Se usa internamente cuando hay like mutuo
    Match createMatch(Long userAId, Long userBId);

    // Cancelar un match existente
    void cancelMatch(Long userAId, Long userBId);

    // Obtener todos los matches activos de un usuario
    List<Match> getMatchesForUser(Long userId);

    // Verificar si existe match activo entre dos usuarios
    boolean existsMatchBetween(Long userAId, Long userBId);

    // Obtener match específico activo entre dos usuarios (opcional)
    Optional<Match> getMatchBetween(Long userAId, Long userBId);
}
