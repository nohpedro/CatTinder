package com.example.cattinder.swipe.dto;

public class MatchDTO
{
    private String userA;
    private String userB;
    private String State;
    private String createdAt;

    public MatchDTO(){}

    public MatchDTO(String userA, String userB, String state, String createdAt)
    {
        this.userA = userA;
        this.userB = userB;
        this.State = state;
        this.createdAt = createdAt;
    }

    public String getUserA() {return userA;}
    public void setUserA(String userA) {this.userA = userA;}


    public String getUserB() {return userB;}
    public void setUserB(String userB) {this.userB = userB;}

    public String getState() { return State; }
    public void setState(String state) { this.State = state; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
