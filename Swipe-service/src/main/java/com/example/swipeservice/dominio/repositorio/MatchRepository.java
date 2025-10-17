package com.example.swipeservice.dominio.repositorio;

import com.example.swipeservice.dominio.entidad.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchRepository extends JpaRepository<MatchEntity, Long> {
    Optional<MatchEntity> findByUsuarioAAndUsuarioB(String usuarioA, String usuarioB);
    boolean existsByUsuarioAAndUsuarioB(String usuarioA, String usuarioB);
}
