package com.example.swipeservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "SwipeDto", description = "Solicitud de swipe")
public class SwipeDto {

    @NotBlank(message = "dir es requerido")
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
