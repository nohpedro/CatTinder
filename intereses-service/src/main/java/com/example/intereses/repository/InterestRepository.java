package com.example.intereses.repository;

import com.example.intereses.model.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {
    
    /**
     * Busca todos los intereses de un usuario específico
     */
    List<Interest> findByUserId(Long userId);
    
    /**
     * Busca intereses por nombre (búsqueda parcial, case-insensitive)
     */
    List<Interest> findByNombreContainingIgnoreCase(String nombre);
    
    /**
     * Busca intereses por nombre exacto (case-insensitive)
     */
    List<Interest> findByNombreIgnoreCase(String nombre);
    
    /**
     * Verifica si existe un interés con el mismo nombre para un usuario
     */
    boolean existsByNombreIgnoreCaseAndUserId(String nombre, Long userId);
    
    /**
     * Verifica si dos usuarios tienen intereses en común (equivalente a un "match" de intereses)
     */
    @Query(value = "SELECT EXISTS(" +
            "  SELECT 1 FROM interests i1" +
            "  INNER JOIN interests i2 ON i1.nombre = i2.nombre" +
            "  WHERE i1.user_id = :userId1 AND i2.user_id = :userId2" +
            ")", nativeQuery = true)
    boolean tienenInteresesEnComun(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
    
    /**
     * Obtiene la lista de intereses compartidos entre dos usuarios
     * Usa JPQL en lugar de SQL nativo para mejor compatibilidad con JPA
     */
    @Query("SELECT DISTINCT i1 FROM Interest i1, Interest i2 " +
            "WHERE i1.nombre = i2.nombre AND i1.userId = :userId1 AND i2.userId = :userId2")
    List<Interest> encontrarInteresesCompartidos(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

}
