package com.example.swipeservice.dto;
import java.time.*;
public class MatchDto {
    private String UsuarioA;
    private String UsuarioB;
    private String Estado; //match activo
    private String FechaCreacion;

    public MatchDto(String UsuarioA,String UsuarioB,String Estado) {
        this.UsuarioA = UsuarioA;
        this.UsuarioB = UsuarioB;
        this.Estado = Estado;
        this.FechaCreacion = LocalDateTime.now().toString();
    }

    public String getUsuarioA(){return this.UsuarioA;}

    public void setUsuarioA(String UsuarioA){this.UsuarioA = UsuarioA;}

    public String getUsuarioB(){return this.UsuarioB;}

    public void setUsuarioB(String UsuarioB){this.UsuarioB = UsuarioB;}

    public String getEstado(){return this.Estado;}

    public void setEstado(String Estado){this.Estado = Estado;}

    public String getFechaCreacion(){return this.FechaCreacion;}

}
