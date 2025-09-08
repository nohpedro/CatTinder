package com.example.swipeservice.docs.schemas;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "SwipeRequest", description = "Cuerpo para registrar un swipe")
public class SwipeRequest {
    @Schema(description = "Dirección del swipe", requiredMode = Schema.RequiredMode.REQUIRED)
    private SwipeDirection dir;

    public SwipeDirection getDir() {
        return dir;
    }
    public void setDir(SwipeDirection dir) {
        this.dir = dir;
    }
}
