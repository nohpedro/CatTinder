
package com.example.configms.api;

import com.example.configms.dto.UserPrefsRequest;
import com.example.configms.dto.UserPrefsResponse;
import com.example.configms.Aplication.PrefsService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prefs")
public class PrefsController {
  private final PrefsService svc;
  public PrefsController(PrefsService svc){ this.svc = svc; }
@PostMapping("/{userId}")
@ResponseStatus(HttpStatus.CREATED)
public UserPrefsResponse save(@PathVariable String userId,
                              @Valid @RequestBody UserPrefsRequest req) {
  if (req.getUserId() == null || !userId.equals(req.getUserId())) {
    req.setUserId(userId);
  }
  return svc.upsert(req);
}
@GetMapping("/{userId}")
public UserPrefsResponse get(@PathVariable String userId,
                             @RequestParam(defaultValue = "false") boolean strict) {
    return svc.get(userId, strict);
  }
  // Evidencia de Criteria Query
  @GetMapping("/search")
  public List<UserPrefsResponse> search(
      @RequestParam(required = false) Boolean darkMode,
      @RequestParam(required = false) Boolean showOnlineStatus,
      @RequestParam(required = false) Boolean pushNotifications,
      @RequestParam(required = false) Boolean showInfo
  ) {
    return svc.search(darkMode, showOnlineStatus, pushNotifications, showInfo);
  }

  // Evidencia de Native Query
  @GetMapping("/native/{userId}")
  public UserPrefsResponse getNative(@PathVariable String userId) {
    return svc.getNative(userId);
  }

  


}
