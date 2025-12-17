package com.example.matches.service;

import com.example.matches.dto.SwipeDTO;
import com.example.matches.model.Swipe;
import com.example.matches.repository.SwipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SwipeServiceImpl implements SwipeService {

    private final SwipeRepository swipeRepository;
    private final MatchService matchService;

    public SwipeServiceImpl(SwipeRepository swipeRepository,
                            MatchService matchService) {
        this.swipeRepository = swipeRepository;
        this.matchService = matchService;
    }

    // -------------------------------------------------
    // Crear Swipe (like / dislike) y verificar like mutuo
    // -------------------------------------------------
    @Transactional
    @Override
    public Swipe createSwipe(SwipeDTO swipeDTO) {

        // Evitar swipe duplicado
        Optional<Swipe> existingSwipe = swipeRepository.findByFromUserIdAndToUserId(
                swipeDTO.getFromUserId(),
                swipeDTO.getToUserId()
        );

        if (existingSwipe.isPresent()) {
            return existingSwipe.get();
        }

        // Crear nuevo swipe
        Swipe swipe = new Swipe(
                swipeDTO.getFromUserId(),
                swipeDTO.getToUserId(),
                swipeDTO.getLiked()
        );

        Swipe savedSwipe = swipeRepository.save(swipe);

        // Si fue like, verificar like mutuo
        if (savedSwipe.isLiked()) {  // <-- aquí cambiamos getLiked() por isLiked()
            boolean reciprocalLike = swipeRepository.existsByFromUserIdAndToUserIdAndLikedTrue(
                    swipeDTO.getToUserId(),
                    swipeDTO.getFromUserId()
            );

            if (reciprocalLike) {
                matchService.createMatch(
                        swipeDTO.getFromUserId(),
                        swipeDTO.getToUserId()
                );
            }
        }

        return savedSwipe;
    }

    // -------------------------------------------------
    // Obtener swipe específico
    // -------------------------------------------------
    @Transactional(readOnly = true)
    @Override
    public Optional<Swipe> getSwipe(Long fromUserId, Long toUserId) {
        return swipeRepository.findByFromUserIdAndToUserId(fromUserId, toUserId);
    }

    // -------------------------------------------------
    // Obtener todos los swipes hechos por un usuario
    // -------------------------------------------------
    @Transactional(readOnly = true)
    @Override
    public List<Swipe> getSwipesByUser(Long fromUserId) {
        return swipeRepository.findAllByFromUserId(fromUserId);
    }
}
