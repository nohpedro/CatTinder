package com.example.cattinder.swipe;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/swipes")
public class SwipeController {

    private final SwipeService service;

    public SwipeController(SwipeService service) {
        this.service = service;
    }

    /**
     * Registra un swipe.
     * POST /swipes/{actorId}/{targetId}
     * Body: { "dir": "like" | "nope" | "superlike" }
     * Respuesta: { ok, dir, matched, matchId? }
     */
    @PostMapping("/{actorId}/{targetId}")
    public Map<String, Object> swipe(@PathVariable String actorId,
                                     @PathVariable String targetId,
                                     @RequestBody Map<String, Object> body) throws Exception {

        Object raw = body.get("dir");
        String dir = raw == null ? "" : raw.toString().toLowerCase();

        if (!(dir.equals("like") || dir.equals("nope") || dir.equals("superlike"))) {
            return Map.of("ok", false, "error", "dir debe ser like|nope|superlike");
        }

        String matchId = service.handleSwipeAndMaybeMatch(actorId, targetId, dir);
        boolean matched = (matchId != null);

        return Map.of(
                "ok", true,
                "dir", dir,
                "matched", matched,
                "matchId", matched ? matchId : ""
        );
    }

    /**
     * Obtiene el documento de match (si existe) entre dos usuarios.
     * GET /swipes/match/{uidA}/{uidB}
     * Respuesta: {} si no existe, o el JSON del match si existe.
     */
    @GetMapping("/match/{uidA}/{uidB}")
    public Map<String, Object> getMatch(@PathVariable String uidA,
                                        @PathVariable String uidB) throws Exception {
        return service.getMatchDoc(uidA, uidB);
    }
}
