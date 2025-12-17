package com.example.matches.repository;

import com.example.matches.model.Swipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SwipeRepository extends JpaRepository<Swipe, Long> {

    // Swipe específico (para evitar duplicados)
    Optional<Swipe> findByFromUserIdAndToUserId(Long fromUserId, Long toUserId);

    // Verificar si existe like de A hacia B
    boolean existsByFromUserIdAndToUserIdAndLikedTrue(Long fromUserId, Long toUserId);

    // Todos los swipes hechos por un usuario
    List<Swipe> findAllByFromUserId(Long fromUserId);

    // Todos los swipes recibidos por un usuario
    List<Swipe> findAllByToUserId(Long toUserId);

    // Todos los likes hechos por un usuario
    List<Swipe> findAllByFromUserIdAndLikedTrue(Long fromUserId);
}
