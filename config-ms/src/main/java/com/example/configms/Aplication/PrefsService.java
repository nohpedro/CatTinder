package com.example.configms.Aplication;

import com.example.configms.dto.UserPrefsRequest;
import com.example.configms.dto.UserPrefsResponse;
import com.example.configms.exception.ExcepcionNoEncontrado;
import com.example.configms.exception.ExcepcionSolicitudInvalida;
import com.example.configms.persistence.UserPrefsEntity;
import com.example.configms.persistence.UserPrefsRepository;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PrefsService {

  private final UserPrefsRepository repo;

  public PrefsService(UserPrefsRepository repo) { this.repo = repo; }

  @Transactional(readOnly = true)
  public UserPrefsResponse get(String userId, boolean strict) {
    var ent = repo.findByUserId(userId)
        .orElseThrow(() -> new ExcepcionNoEncontrado("usuario no existe"));
    return toResponse(ent);
  }

  @Transactional
  public UserPrefsResponse upsert(UserPrefsRequest req) {
    if (req.getUserId() == null || req.getUserId().isBlank()) {
      throw new ExcepcionSolicitudInvalida("userId requerido");
    }
    var ent = repo.findByUserId(req.getUserId()).orElseGet(UserPrefsEntity::new);
    ent.setUserId(req.getUserId());
    ent.setDarkMode(req.getDarkMode());
    ent.setShowOnlineStatus(req.getShowOnlineStatus());
    ent.setPushNotifications(req.getPushNotifications());
    ent.setShowInfo(req.getShowInfo());

    var saved = repo.save(ent);
    return toResponse(saved);
  }
    @Transactional(readOnly = true)
  public UserPrefsResponse getNative(String userId) {
    var ent = repo.findNativeByUserId(userId)
        .orElseThrow(() -> new ExcepcionNoEncontrado("usuario no existe"));
    return toResponse(ent);
  }
  @Transactional(readOnly = true)
  public List<UserPrefsResponse> search(Boolean darkMode, Boolean showOnlineStatus,
                                        Boolean pushNotifications, Boolean showInfo) {
    return repo.searchByFilters(darkMode, showOnlineStatus, pushNotifications, showInfo)
        .stream().map(PrefsService::toResponse).toList();
  }
  private static UserPrefsResponse toResponse(UserPrefsEntity e) {
    var r = new UserPrefsResponse();
    r.setUserId(e.getUserId());
    r.setDarkMode(e.getDarkMode());
    r.setShowOnlineStatus(e.getShowOnlineStatus());
    r.setPushNotifications(e.getPushNotifications());
    r.setShowInfo(e.getShowInfo());
    r.setUpdatedAt(e.getUpdatedAt());
    return r;
  }
}
