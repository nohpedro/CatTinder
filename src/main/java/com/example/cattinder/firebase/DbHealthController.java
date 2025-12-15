package com.example.cattinder.firebase;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DbHealthController {

    private final Firestore db;

    @GetMapping("/health/db")
    public ResponseEntity<Map<String, Object>> health() {
        try {
            var ref = db.collection("__health").document("ping");
            ref.set(Map.of("ts", Timestamp.now())).get(); // write
            var snap = ref.get().get();                   // read
            if (!snap.exists()) {
                throw new IllegalStateException("No se pudo leer el doc de health");
            }
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
