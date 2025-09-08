package com.example.cattinder.users.preference.service;

import com.example.cattinder.users.preference.dto.PreferenceDTO;
import com.example.cattinder.users.preference.model.Preference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PreferenceServiceTest {

    private PreferenceServiceImpl preferenceService;

    @BeforeEach
    void setUp() {
        preferenceService = new PreferenceServiceImpl();
    }

    @Test
    void testCreatePreference() {
        PreferenceDTO dto = new PreferenceDTO(1L, "Matemáticas", "Grupo", "Mañana");
        Preference preference = preferenceService.createPreference(dto);

        assertNotNull(preference.getId());
        assertEquals(1L, preference.getUserId());
        assertEquals("Matemáticas", preference.getSubject());
        assertEquals("Grupo", preference.getStudyStyle());
        assertEquals("Mañana", preference.getAvailability());
    }

    @Test
    void testGetPreferencesByUserId() {
        PreferenceDTO dto1 = new PreferenceDTO(1L, "Historia", "Individual", "Tarde");
        PreferenceDTO dto2 = new PreferenceDTO(1L, "Inglés", "Grupo", "Mañana");
        preferenceService.createPreference(dto1);
        preferenceService.createPreference(dto2);

        List<Preference> preferences = preferenceService.getPreferencesByUserId(1L);

        assertEquals(2, preferences.size());
        assertTrue(preferences.stream().anyMatch(p -> p.getSubject().equals("Historia")));
        assertTrue(preferences.stream().anyMatch(p -> p.getSubject().equals("Inglés")));
    }

    @Test
    void testUpdatePreference() {
        PreferenceDTO dto = new PreferenceDTO(1L, "Química", "Grupo", "Noche");
        Preference preference = preferenceService.createPreference(dto);

        PreferenceDTO updatedDto = new PreferenceDTO(1L, "Biología", "Individual", "Mañana");
        Preference updated = preferenceService.updatePreference(preference.getId(), updatedDto);

        assertEquals("Biología", updated.getSubject());
        assertEquals("Individual", updated.getStudyStyle());
        assertEquals("Mañana", updated.getAvailability());
    }

    @Test
    void testDeletePreference() {
        PreferenceDTO dto = new PreferenceDTO(1L, "Física", "Grupo", "Tarde");
        Preference preference = preferenceService.createPreference(dto);

        preferenceService.deletePreference(preference.getId());

        List<Preference> preferences = preferenceService.getPreferencesByUserId(1L);
        assertTrue(preferences.isEmpty());
    }
}
