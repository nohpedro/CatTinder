package com.example.confgms.Aplication;

import com.example.confgms.dto.configDto;
import com.example.confgms.dominio.entidad.confEntity;
import com.example.confgms.dominio.repositorio.ConfigRepository;
import com.example.confgms.excepcion.Error.ExcepcionNoEncontrado;
import com.example.confgms.excepcion.Error.ExcepcionSolicitudInvalida;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class configService {

    private final ConfigRepository repo;

    public configService(ConfigRepository repo) {
        this.repo = repo;
    }

    private static String norm(String s) { return s == null ? null : s.trim().toLowerCase(); }

    // --------- CREATE (POST) ----------
    @Transactional
    public configDto crear(configDto dto) {
        String uid = norm(dto.getUserid());
        if (uid == null || uid.isEmpty())
            throw new ExcepcionSolicitudInvalida("userid es requerido");
        if (repo.existsByUseridIgnoreCase(uid))
            throw new ExcepcionSolicitudInvalida("Ya existe configuración para: " + uid);

        confEntity e = fromDto(uid, dto, /*defaultsIfNull*/ true);
        repo.save(e);
        return toDto(e);
    }

    // --------- READ (GET /{userId}) ----------
    @Transactional(readOnly = true)
    public configDto obtener(String userId) {
        String uid = norm(userId);
        confEntity e = repo.findByUseridIgnoreCase(uid)
                .orElseThrow(() -> new ExcepcionNoEncontrado("No existe configuración para: " + uid));
        return toDto(e);
    }

    // --------- UPSERT / REPLACE (PUT /{userId}) ----------
    @Transactional
    public configDto upsert(String userId, configDto dto) {
        String uid = norm(userId);
        if (uid == null || uid.isEmpty())
            throw new ExcepcionSolicitudInvalida("userid es requerido");

        confEntity e = repo.findByUseridIgnoreCase(uid).orElse(new confEntity());
        e.setUserid(uid);

        // PUT reemplaza todo: si vienen null, se asumen false por defecto
        e.setDarkmode(Boolean.TRUE.equals(dto.getDarkmode()));
        e.setShowInfo(Boolean.TRUE.equals(dto.getShowInfo()));
        e.setShowOnlineStatus(Boolean.TRUE.equals(dto.getShowOnlineStatus()));
        e.setShowNotifications(Boolean.TRUE.equals(dto.getShowNotifications()));

        repo.save(e);
        return toDto(e);
    }

    // --------- PATCH (PATCH /{userId}) ----------
    @Transactional
    public configDto patch(String userId, configDto partial) {
        String uid = norm(userId);
        confEntity e = repo.findByUseridIgnoreCase(uid)
                .orElseThrow(() -> new ExcepcionNoEncontrado("No existe configuración para: " + uid));

        // Solo actualizar campos no nulos
        if (partial.getDarkmode() != null) e.setDarkmode(partial.getDarkmode());
        if (partial.getShowInfo() != null) e.setShowInfo(partial.getShowInfo());
        if (partial.getShowOnlineStatus() != null) e.setShowOnlineStatus(partial.getShowOnlineStatus());
        if (partial.getShowNotifications() != null) e.setShowNotifications(partial.getShowNotifications());

        repo.save(e);
        return toDto(e);
    }

    // --------- DELETE (DELETE /{userId}) ----------
    @Transactional
    public void eliminar(String userId) {
        String uid = norm(userId);
        confEntity e = repo.findByUseridIgnoreCase(uid)
                .orElseThrow(() -> new ExcepcionNoEncontrado("No existe configuración para: " + uid));
        repo.delete(e);
    }

    // --------- Helpers internos (sin mapper externo) ----------
    private static configDto toDto(confEntity e) {
        return new configDto(
                e.getUserid(),
                e.isDarkmode(),
                e.isShowInfo(),
                e.isShowOnlineStatus(),
                e.isShowNotifications()
        );
    }

    /**
     * Construye entidad desde DTO.
     * Si defaultsIfNull=true, los null se toman como false (útil para POST/PUT).
     */
    private static confEntity fromDto(String userid, configDto d, boolean defaultsIfNull) {
        boolean dark = valueOrDefault(d.getDarkmode(), defaultsIfNull);
        boolean info = valueOrDefault(d.getShowInfo(), defaultsIfNull);
        boolean online = valueOrDefault(d.getShowOnlineStatus(), defaultsIfNull);
        boolean notif = valueOrDefault(d.getShowNotifications(), defaultsIfNull);
        return new confEntity(userid, dark, info, online, notif);
    }

    private static boolean valueOrDefault(Boolean v, boolean defaultsIfNull) {
        return (v != null) ? v : (defaultsIfNull ? false : false);
    }
}
