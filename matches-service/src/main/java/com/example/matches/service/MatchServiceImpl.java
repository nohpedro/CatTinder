package com.example.matches.service;

import com.example.matches.exception.MatchNotFoundException;
import com.example.matches.model.Match;
import com.example.matches.repository.MatchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;

    public MatchServiceImpl(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    // -----------------------
    // CREAR MATCH (like mutuo)
    // -----------------------
    @Transactional
    @Override
    public Match createMatch(Long userAId, Long userBId) {
        Long user1 = Math.min(userAId, userBId);
        Long user2 = Math.max(userAId, userBId);

        // Evitar duplicados activos
        Optional<Match> existing = matchRepository.findByUser1IdAndUser2IdAndActiveTrue(user1, user2);
        if (existing.isPresent()) {
            return existing.get();
        }

        Match match = new Match(userAId, userBId);
        return matchRepository.save(match);
    }

    // -----------------------
    // CANCELAR MATCH
    // -----------------------
    @Transactional
    @Override
    public void cancelMatch(Long userAId, Long userBId) {
        Long user1 = Math.min(userAId, userBId);
        Long user2 = Math.max(userAId, userBId);

        Match match = matchRepository.findByUser1IdAndUser2IdAndActiveTrue(user1, user2)
                .orElseThrow(() -> new MatchNotFoundException("No existe match activo entre estos usuarios"));

        match.cancel();
        matchRepository.save(match);
    }

    // -----------------------
    // OBTENER MATCHES DE UN USUARIO
    // -----------------------
    @Transactional(readOnly = true)
    @Override
    public List<Match> getMatchesForUser(Long userId) {
        return matchRepository.findByUser1IdAndActiveTrueOrUser2IdAndActiveTrue(userId, userId);
    }

    // -----------------------
    // EXISTENCIA DE MATCH ENTRE DOS USUARIOS
    // -----------------------
    @Transactional(readOnly = true)
    @Override
    public boolean existsMatchBetween(Long userAId, Long userBId) {
        Long user1 = Math.min(userAId, userBId);
        Long user2 = Math.max(userAId, userBId);

        return matchRepository.findByUser1IdAndUser2IdAndActiveTrue(user1, user2).isPresent();
    }

    // -----------------------
    // OBTENER MATCH ESPECÍFICO ENTRE DOS USUARIOS
    // -----------------------
    @Transactional(readOnly = true)
    @Override
    public Optional<Match> getMatchBetween(Long userAId, Long userBId) {
        Long user1 = Math.min(userAId, userBId);
        Long user2 = Math.max(userAId, userBId);

        return matchRepository.findByUser1IdAndUser2IdAndActiveTrue(user1, user2);
    }
}
