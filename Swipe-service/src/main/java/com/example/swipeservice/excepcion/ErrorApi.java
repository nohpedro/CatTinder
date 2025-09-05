package com.example.swipeservice.excepcion;

import java.time.Instant;

public class ErrorApi {
    private Instant marcaTiempo = Instant.now();
    private int estado;
    private String error;
    private String mensaje;
    private String ruta;

    public Instant getMarcaTiempo() { return marcaTiempo; }
    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public String getRuta() { return ruta; }
    public void setRuta(String ruta) { this.ruta = ruta; }
}