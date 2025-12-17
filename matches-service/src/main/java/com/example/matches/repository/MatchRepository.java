package com.example.matches.repository;

import com.example.matches.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {

    // Match activo entre dos usuarios
    Optional<Match> findByUser1IdAndUser2IdAndActiveTrue(Long user1Id, Long user2Id);

    // Todos los matches activos de un usuario
    List<Match> findByUser1IdAndActiveTrueOrUser2IdAndActiveTrue(Long user1Id, Long user2Id);
}
