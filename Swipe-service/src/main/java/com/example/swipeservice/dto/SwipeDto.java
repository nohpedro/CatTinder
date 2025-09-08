package com.example.swipeservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "SwipeDto", description = "Solicitud de swipe")
public class SwipeDto {

    @Schema(
            description = "Dirección del swipe",
            example = "right",
            allowableValues = {"right","left","like"}
    )
    private String dir;

    public SwipeDto() {}

    public String getDir() { return dir; }
    public void setDir(String dir) { this.dir = dir; }
}
