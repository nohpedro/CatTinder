package com.example.swipeservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(name = "SwipeDto", description = "Solicitud de swipe")
public class SwipeDto {

    @Schema(
            description = "Dirección del swipe",
            example = "right",
            allowableValues = {"right","left","like"}
    )
    @NotBlank(message = "La dirección del swipe (dir) no puede estar vacía")
    @Pattern(
            regexp = "right|left|like",
            message = "No puede estar vacio"
    )
    private String dir;

    public SwipeDto() {}

    public String getDir() { return dir; }
    public void setDir(String dir) { this.dir = dir; }
}
