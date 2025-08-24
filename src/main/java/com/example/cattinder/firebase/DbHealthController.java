package com.example.cattinder.firebase;

import com.google.cloud.firestore.Firestore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@RestController
public class DbHealthController {
    private final Firestore db;

    public DbHealthController(Firestore db) {
        this.db = db;
    }
    @GetMapping("/health/db")
    public Map<String, Object> Health() throws Exception {
        db.collection("health")
                .document("ping")
                .get();

        return Map.of("status", "ok");
    }
}