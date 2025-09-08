package com.example.cattinder.firebase;

import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DbHealthController {

    @Autowired(required = false)
    private Firestore db;

    @Value("${firebase.enabled:false}")
    private boolean enabled;

    @GetMapping("/health/db")
    public ResponseEntity<Map<String, Object>> health() {
        if (!enabled || db == null) {
            return ResponseEntity.ok(Map.of(
                    "status", "DISABLED",
                    "message", "Firebase is disabled"
            ));
        }

        try {
            var ref = db.collection("__health").document("ping");
            ref.set(Map.of("ts", com.google.cloud.Timestamp.now())).get();
            var snap = ref.get().get();
            if (!snap.exists()) throw new IllegalStateException("No se pudo leer el doc de health");

            return ResponseEntity.ok(Map.of(
                    "status", "OK",
                    "ts", String.valueOf(snap.get("ts"))
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "ERROR",
                    "message", e.getMessage()
            ));
        }
    }
}
