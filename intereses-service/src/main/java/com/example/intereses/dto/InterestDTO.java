package com.example.intereses.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterestDTO {
    
    @NotBlank(message = "El nombre del interés es obligatorio")
    private String nombre;
    
    private String descripcion;
    
    private Long userId;
}

