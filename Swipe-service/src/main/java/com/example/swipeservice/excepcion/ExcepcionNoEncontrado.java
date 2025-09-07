package com.example.swipeservice.excepcion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ExcepcionNoEncontrado extends RuntimeException {
    public ExcepcionNoEncontrado() {
        super("Recurso no encontrado");
    }
    public ExcepcionNoEncontrado(String message) {
        super(message);
    }
}
