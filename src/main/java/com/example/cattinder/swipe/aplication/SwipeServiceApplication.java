package com.example.cattinder.swipe.aplication;

import com.example.cattinder.swipe.dto.MatchDTO;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class SwipeServiceApplication {
    private final Firestore db;
    public SwipeServiceApplication(Firestore db) {
        this.db = db;
    }
    //utieles
    private static String swipeId(String actorId, String targetId){
        return actorId + "-" + targetId;
    }

    private static String MatchId(String a, String b){
        return (a.compareTo(b) < 0) ? a+"_"+b : b+"_"+a;
    }

    private static boolean isPositive(String dir){
        if(dir==null) return false;
        String d=dir.toLowerCase();
        return d.equals("like");
    }

    //swipes
    public void saveSwipe(String actorId, String targetId, String dir)
            throws ExecutionException, InterruptedException {
        String norm = dir.toLowerCase();

        Map<String,Object> swipe = new HashMap<>();
        swipe.put("actorId", actorId);
        swipe.put("targetId", targetId);
        swipe.put("dir", norm);
        swipe.put("ts", Timestamp.now());

        db.collection("swipes")
                .document(swipeId(actorId, targetId))
                .set(swipe, SetOptions.merge())
                .get();
    }

    private String getSwipeDir(String actorId, String targetId)
            throws ExecutionException, InterruptedException {
        var snap = db.collection("swipes").document(swipeId(actorId, targetId))
                .get().get();
        if (!snap.exists()) return null;
        var v = snap.get("dir");
        return v != null ? v.toString() : null;
    }

    /* ===== matches ===== */
    public boolean ensureMatch(String uidA, String uidB)
            throws ExecutionException, InterruptedException {
        String id = MatchId(uidA, uidB);
        var ref = db.collection("matches").document(id);
        var snap = ref.get().get();
        if (snap.exists()) return false;

        String userA = (uidA.compareTo(uidB) < 0) ? uidA : uidB;
        String userB = (uidA.compareTo(uidB) < 0) ? uidB : uidA;

        Map<String,Object> match = new HashMap<>();
        match.put("userA", userA);
        match.put("userB", userB);
        match.put("state", "active");
        match.put("createdAt", Timestamp.now());

        ref.set(match).get();
        return true;
    }

    /**
     * Registra el swipe y, si hay reciprocidad positiva, crea match.
     * @return matchId si hay match; null en caso contrario.
     */
    public String handleSwipeAndMaybeMatch(String actorId, String targetId, String dir)
            throws ExecutionException, InterruptedException {
        saveSwipe(actorId, targetId, dir);
        if (!isPositive(dir)) return null;

        String back = getSwipeDir(targetId, actorId);
        if (isPositive(back)) {
            ensureMatch(actorId, targetId);
            return MatchId(actorId, targetId);
        }
        return null;
    }

    public MatchDTO getMatch(String uidA, String uidB)
            throws ExecutionException, InterruptedException {
        String id = MatchId(uidA, uidB);
        var snap = db.collection("matches").document(id).get().get();
        if (!snap.exists()) return null;

        String userA = String.valueOf(snap.get("userA"));
        String userB = String.valueOf(snap.get("userB"));
        String state = String.valueOf(snap.get("state"));
        Timestamp ts = (Timestamp) snap.get("createdAt");
        String iso = ts != null ? Instant.ofEpochSecond(ts.getSeconds(), ts.getNanos()).toString() : null;

        return new MatchDTO(userA, userB, state, iso);
    }
}
