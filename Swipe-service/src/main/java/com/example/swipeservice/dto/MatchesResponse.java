package com.example.swipeservice.dto;

import java.util.List;

public class MatchesResponse {
    private String userId;
    private List<String> matches;

    public MatchesResponse(String userId, List<String> matches) {
        this.userId = userId;
        this.matches = matches;
    }
    public String getUserId() { return userId; }
    public List<String> getMatches() { return matches; }
}
