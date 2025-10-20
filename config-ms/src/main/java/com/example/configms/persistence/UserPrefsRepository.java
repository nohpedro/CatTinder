package com.example.configms.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserPrefsRepository
    extends JpaRepository<UserPrefsEntity, Long>, UserPrefsRepositoryCustom {

  // Derived
  Optional<UserPrefsEntity> findByUserId(String userId);

  // Native
  @Query(value = "SELECT * FROM user_prefs WHERE user_id = :userId", nativeQuery = true)
  Optional<UserPrefsEntity> findNativeByUserId(@Param("userId") String userId);

  // Derived adicional (para evidencia)
  List<UserPrefsEntity> findByShowOnlineStatus(Boolean showOnlineStatus);
}
