package com.example.cattinder.swipe;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class SwipeService {
    private final Firestore db;
    public SwipeService(Firestore db) {
        this.db = db;
    }

    public void saveSwipe(String actorId, String targetId, String dir)
        throws ExecutionException,InterruptedException{
        String normalizedDir = dir.toLowerCase();
        Map<String,Object> swipe = Map.of(
                "actorId", actorId,
                "targetId", targetId,
                "dir", normalizedDir,
                "ts", Timestamp.now());
        db.collection("swipes")
                .document(SwipeIds.swipeId(actorId,targetId))
                .set(swipe,SetOptions.merge())
                .get();
    }

    public String getSwipeDir(String actorId, String targetId)
            throws ExecutionException, InterruptedException {
        DocumentSnapshot snap = db.collection("swipes")
                .document(SwipeIds.swipeId(actorId, targetId))
                .get().get();
        if (!snap.exists()) return null;
        Object v = snap.get("dir");
        return v != null ? v.toString() : null;
    }

    //matches
    //creacion de matches
    public boolean ensureMatch(String uidA, String uidB)
            throws ExecutionException, InterruptedException {
        String id = SwipeIds.matchId(uidA, uidB);
        DocumentReference ref = db.collection("matches").document(id);
        DocumentSnapshot snap = ref.get().get();

        if (snap.exists()) return false;

        String userA = (uidA.compareTo(uidB) < 0) ? uidA : uidB;
        String userB = (uidA.compareTo(uidB) < 0) ? uidB : uidA;

        Map<String, Object> match = Map.of(
                "userA", userA,
                "userB", userB,
                "state", "active",
                "createdAt", Timestamp.now()
        );
        ref.set(match).get();
        return true;
    }
    //save swipe
    public String handleSwipeAndMaybeMatch(String actorId, String targetId, String dir)
            throws ExecutionException, InterruptedException {
        saveSwipe(actorId, targetId, dir);

        if (!SwipeIds.isPositive(dir)) {
            return null; // con "nope" nunca hay match
        }

        String backDir = getSwipeDir(targetId, actorId);
        if (SwipeIds.isPositive(backDir)) {
            ensureMatch(actorId, targetId);
            return SwipeIds.matchId(actorId, targetId);
        }
        return null;
    }
    public Map<String, Object> getMatchDoc(String uidA, String uidB)
            throws ExecutionException, InterruptedException {
        String id = SwipeIds.matchId(uidA, uidB);
        var snap = db.collection("matches").document(id).get().get();
        return snap.exists() ? snap.getData() : Map.of();
    }
}
