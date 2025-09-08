package com.example.swipeservice.docs.schemas;

import io.swagger.v3.oas.annotations.media.Schema;
@Schema(description = "Direccion de swipe")
public enum SwipeDirection {
    @Schema(description = "Like / aprobación")
    LIKE,
    @Schema(description = "Dislike / rechazo")
    DISLIKE
}
