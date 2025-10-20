package com.example.configms.exception;

import org.springframework.http.HttpStatus;

public class ErrorApi {
  private int estado;
  private String error;
  private String mensaje;
  private String ruta;

  public static ErrorApi of(HttpStatus status, String error, String mensaje, String ruta) {
    ErrorApi e = new ErrorApi();
    e.estado = status.value();
    e.error = error;
    e.mensaje = mensaje;
    e.ruta = ruta;
    return e;
  }

  public int getEstado() { return estado; }
  public void setEstado(int estado) { this.estado = estado; }

  public String getError() { return error; }
  public void setError(String error) { this.error = error; }

  public String getMensaje() { return mensaje; }
  public void setMensaje(String mensaje) { this.mensaje = mensaje; }

  public String getRuta() { return ruta; }
  public void setRuta(String ruta) { this.ruta = ruta; }
}