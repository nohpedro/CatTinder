package com.example.swipeservice.dominio.repositorio;

import com.example.swipeservice.dominio.entidad.SwipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SwipeRepository extends JpaRepository<SwipeEntity, Long> {
    Optional<SwipeEntity> findByActorAndObjetivo(String actor, String objetivo);
}
