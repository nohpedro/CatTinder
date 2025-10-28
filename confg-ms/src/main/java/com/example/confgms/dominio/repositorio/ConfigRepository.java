package com.example.confgms.dominio.repositorio;

import com.example.confgms.dominio.entidad.confEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repo de configuración de usuario.
 * - ID es el userid (String).
 * - Incluye consultas case-insensitive y toggles parciales.
 */
public interface ConfigRepository extends JpaRepository<confEntity, String> {

    // ------------------ Lookups (case-insensitive) ------------------

    Optional<confEntity> findByUseridIgnoreCase(String userid);

    boolean existsByUseridIgnoreCase(String userid);

    // ------------------ EXISTS nativo (similar a tu ejemplo) ------------------

    @Query(value = """
        SELECT EXISTS(
          SELECT 1
          FROM config c
          WHERE LOWER(c.userid) = LOWER(:uid)
        )
        """, nativeQuery = true)
    boolean existeConfigPara(@Param("uid") String uid);

    // ------------------ Proyección de flags (ligera) ------------------

    interface FlagsView {
        boolean getDarkmode();
        boolean getShowInfo();
        boolean getShowOnlineStatus();
        boolean getShowNotifications();
    }

    @Query("""
        SELECT c.darkmode AS darkmode,
               c.showInfo AS showInfo,
               c.showOnlineStatus AS showOnlineStatus,
               c.showNotifications AS showNotifications
        FROM confEntity c
        WHERE LOWER(c.userid) = LOWER(:uid)
        """)
    Optional<FlagsView> findFlagsByUserid(@Param("uid") String userid);

    // ------------------ Updates parciales (toggles rápidos) ------------------

    @Modifying
    @Query("""
        UPDATE confEntity c
        SET c.darkmode = :valor
        WHERE LOWER(c.userid) = LOWER(:uid)
        """)
    int setDarkmode(@Param("uid") String userid, @Param("valor") boolean valor);

    @Modifying
    @Query("""
        UPDATE confEntity c
        SET c.showInfo = :valor
        WHERE LOWER(c.userid) = LOWER(:uid)
        """)
    int setShowInfo(@Param("uid") String userid, @Param("valor") boolean valor);

    @Modifying
    @Query("""
        UPDATE confEntity c
        SET c.showOnlineStatus = :valor
        WHERE LOWER(c.userid) = LOWER(:uid)
        """)
    int setShowOnlineStatus(@Param("uid") String userid, @Param("valor") boolean valor);

    @Modifying
    @Query("""
        UPDATE confEntity c
        SET c.showNotifications = :valor
        WHERE LOWER(c.userid) = LOWER(:uid)
        """)
    int setShowNotifications(@Param("uid") String userid, @Param("valor") boolean valor);

    // ------------------ (Opcional) UPSERT nativo Postgres ------------------
    // Úsalo solo si estás en Postgres; para otros motores, mejor .save() en el service.
    @Modifying
    @Query(value = """
        INSERT INTO config (userid, darkmode, show_info, show_online_status, show_notifications)
        VALUES (:uid, :darkmode, :showInfo, :showOnlineStatus, :showNotifications)
        ON CONFLICT (userid) DO UPDATE SET
          darkmode = EXCLUDED.darkmode,
          show_info = EXCLUDED.show_info,
          show_online_status = EXCLUDED.show_online_status,
          show_notifications = EXCLUDED.show_notifications
        """, nativeQuery = true)
    int upsertPostgres(@Param("uid") String userid,
                       @Param("darkmode") boolean darkmode,
                       @Param("showInfo") boolean showInfo,
                       @Param("showOnlineStatus") boolean showOnlineStatus,
                       @Param("showNotifications") boolean showNotifications);
}
