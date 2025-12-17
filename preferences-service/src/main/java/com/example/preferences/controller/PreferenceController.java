package com.example.preferences.controller;

import com.example.preferences.dto.PreferenceDTO;
import com.example.preferences.dto.PreferenceOptionDTO;
import com.example.preferences.dto.PreferenceUserDTO;
import com.example.preferences.model.Preference;
import com.example.preferences.model.PreferenceOption;
import com.example.preferences.model.PreferenceUser;
import com.example.preferences.service.PreferenceOptionService;
import com.example.preferences.service.PreferenceService;
import com.example.preferences.service.PreferenceUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/preferences")
public class PreferenceController {

    private final PreferenceService preferenceService;
    private final PreferenceOptionService optionService;
    private final PreferenceUserService userService;

    public PreferenceController(PreferenceService preferenceService,
                                PreferenceOptionService optionService,
                                PreferenceUserService userService) {
        this.preferenceService = preferenceService;
        this.optionService = optionService;
        this.userService = userService;
    }

    // =========================
    // PREFERENCE (StudyStyle + Availability)
    // =========================

    @PostMapping("/settings")
    public ResponseEntity<Preference> createOrUpdateSettings(@Valid @RequestBody PreferenceDTO dto) {
        Preference pref = preferenceService.createOrUpdateSettings(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pref);
    }

    @GetMapping("/settings/{userId}")
    public ResponseEntity<Preference> getSettingsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(preferenceService.getSettingsByUser(userId));
    }

    // =========================
    // PREFERENCE OPTION (Opciones globales)
    // =========================

    @PostMapping("/options")
    public ResponseEntity<PreferenceOption> createOption(@Valid @RequestBody PreferenceOptionDTO dto) {
        PreferenceOption option = optionService.createOption(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(option);
    }

    @GetMapping("/options")
    public ResponseEntity<List<PreferenceOption>> getAllOptions() {
        return ResponseEntity.ok(optionService.getAllOptions());
    }

    @PutMapping("/options/{id}")
    public ResponseEntity<PreferenceOption> updateOption(@PathVariable Long id,
                                                         @Valid @RequestBody PreferenceOptionDTO dto) {
        return ResponseEntity.ok(optionService.updateOption(id, dto));
    }

    @DeleteMapping("/options/{id}")
    public ResponseEntity<Void> deleteOption(@PathVariable Long id) {
        optionService.deleteOption(id);
        return ResponseEntity.noContent().build();
    }

    // =========================
    // PREFERENCE USER (Relación Usuario-Preferencia)
    // =========================

    @PostMapping("/user")
    public ResponseEntity<PreferenceUser> addUserPreference(@Valid @RequestBody PreferenceUserDTO dto) {
        PreferenceUser pu = userService.addPreferenceToUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pu);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PreferenceUser>> getUserPreferences(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getPreferencesByUser(userId));
    }

    @DeleteMapping("/user/{userId}/option/{optionId}")
    public ResponseEntity<Void> removeUserPreference(@PathVariable Long userId,
                                                     @PathVariable Long optionId) {
        userService.removePreferenceFromUser(userId, optionId);
        return ResponseEntity.noContent().build();
    }
}
