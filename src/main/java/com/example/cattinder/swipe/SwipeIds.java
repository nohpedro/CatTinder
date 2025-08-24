package com.example.cattinder.swipe;

public class SwipeIds {
    private SwipeIds() {}

    static String swipeId(String actorID,String targetID){
        return actorID+"_"+targetID;
    }

    static String matchId(String uidA, String uidB) {
        return (uidA.compareTo(uidB) < 0) ? uidA + "_" + uidB : uidB + "_" + uidA;
    }

    static boolean isPositive(String dir) {
        if (dir == null) return false;
        String d = dir.toLowerCase();
        return d.equals("like");
    }
}
