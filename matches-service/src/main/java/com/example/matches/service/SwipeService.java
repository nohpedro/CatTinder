package com.example.matches.service;

import com.example.matches.dto.SwipeDTO;
import com.example.matches.model.Swipe;

import java.util.List;
import java.util.Optional;

public interface SwipeService {

    // Crear swipe (like / dislike) y crear match si hay like mutuo
    Swipe createSwipe(SwipeDTO swipeDTO);

    // Obtener swipe específico (útil para validaciones)
    Optional<Swipe> getSwipe(Long fromUserId, Long toUserId);

    // Obtener todos los swipes hechos por un usuario
    List<Swipe> getSwipesByUser(Long fromUserId);
}
