package com.example.swipeservice.dominio.repositorio;

import com.example.swipeservice.dominio.entidad.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<MatchEntity, Long> {
    Optional<MatchEntity> findByUsuarioAAndUsuarioB(String usuarioA, String usuarioB);
    boolean existsByUsuarioAAndUsuarioB(String usuarioA, String usuarioB);

    @Query(value = """
      SELECT EXISTS(
        SELECT 1 FROM matches m
        WHERE (m.usuarioA = :u1 AND m.usuarioB = :u2)
           OR (m.usuarioA = :u2 AND m.usuarioB = :u1)
      )
      """, nativeQuery = true)
    boolean existeMatchEntre(@Param("u1") String u1, @Param("u2") String u2);
}
