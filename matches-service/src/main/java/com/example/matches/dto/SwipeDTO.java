package com.example.matches.dto;

import jakarta.validation.constraints.NotNull;

// DTO para realizar un swipe (like / dislike)
public class SwipeDTO {

    @NotNull(message = "El ID del usuario que hace el swipe es obligatorio")
    private Long fromUserId;

    @NotNull(message = "El ID del usuario objetivo es obligatorio")
    private Long toUserId;

    @NotNull(message = "El valor liked es obligatorio")
    private Boolean liked;

    public SwipeDTO() {
    }

    public SwipeDTO(Long fromUserId, Long toUserId, Boolean liked) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.liked = liked;
    }

    // Getters y setters
    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }
}
