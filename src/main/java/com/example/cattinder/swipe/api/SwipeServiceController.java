package com.example.cattinder.swipe.api;

import com.example.cattinder.swipe.aplication.SwipeServiceApplication;
import com.example.cattinder.swipe.dto.MatchDTO;
import com.example.cattinder.swipe.dto.SwipeRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/swipes")
public class SwipeServiceController {

    private final SwipeServiceApplication app;

    public SwipeServiceController(SwipeServiceApplication app) {
        this.app = app;
    }

    /** POST /api/swipes/{actorId}/{targetId}  body: {"dir":"like|nope|superlike"} */
    @PostMapping("/{actorId}/{targetId}")
    public ResponseEntity<Map<String, Object>> swipe(@PathVariable String actorId,
                                                     @PathVariable String targetId,
                                                     @RequestBody(required = false) SwipeRequestDTO body,
                                                     @RequestParam(value = "dir", required = false) String dirParam) throws Exception {
        String dir = (body != null && body.getDir() != null) ? body.getDir()
                : (dirParam != null ? dirParam : "");

        dir = dir.toLowerCase();

        if (!(dir.equals("like") || dir.equals("nope") || dir.equals("superlike"))) {
            return ResponseEntity.badRequest()
                    .body(Map.of("ok", false, "error", "dir debe ser like|nope|superlike"));
        }

        String matchId = app.handleSwipeAndMaybeMatch(actorId, targetId, dir);
        boolean matched = (matchId != null);

        // Map.of no acepta null: construimos a mano
        var resp = new HashMap<String, Object>();
        resp.put("ok", true);
        resp.put("dir", dir);
        resp.put("matched", matched);
        if (matched) resp.put("matchId", matchId);

        return ResponseEntity.ok(resp);
    }

    /** GET /api/swipes/match/{uidA}/{uidB} */
    @GetMapping("/match/{uidA}/{uidB}")
    public ResponseEntity<?> getMatch(@PathVariable String uidA, @PathVariable String uidB) throws Exception {
        MatchDTO dto = app.getMatch(uidA, uidB);
        if (dto == null) return ResponseEntity.ok(Map.of()); // no existe
        return ResponseEntity.ok(dto);
    }
}
