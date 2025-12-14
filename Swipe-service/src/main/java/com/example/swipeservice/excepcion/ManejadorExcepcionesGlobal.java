package com.example.swipeservice.excepcion;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ManejadorExcepcionesGlobal {

    @ExceptionHandler(ExcepcionNoEncontrado.class)
    public ResponseEntity<ErrorApi> noEncontrado(ExcepcionNoEncontrado ex, HttpServletRequest req) {
        ErrorApi err = new ErrorApi();
        err.setEstado(HttpStatus.NOT_FOUND.value());
        err.setError("No Encontrado");
        err.setMensaje(ex.getMessage());
        err.setRuta(req.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(ExcepcionSolicitudInvalida.class)
    public ResponseEntity<ErrorApi> solicitudInvalida(ExcepcionSolicitudInvalida ex, HttpServletRequest req) {
        ErrorApi err = new ErrorApi();
        err.setEstado(HttpStatus.BAD_REQUEST.value());
        err.setError("Solicitud Incorrecta");
        err.setMensaje(ex.getMessage());
        err.setRuta(req.getRequestURI());
        return ResponseEntity.badRequest().body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorApi> validacion(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .findFirst().orElse("Datos inválidos");
        ErrorApi err = new ErrorApi();
        err.setEstado(HttpStatus.BAD_REQUEST.value());
        err.setError("Solicitud Incorrecta");
        err.setMensaje(msg);
        err.setRuta(req.getRequestURI());
        return ResponseEntity.badRequest().body(err);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorApi> generica(Exception ex, HttpServletRequest req) {
        ErrorApi err = new ErrorApi();
        err.setEstado(HttpStatus.INTERNAL_SERVER_ERROR.value());
        err.setError("Error Interno");
        err.setMensaje(ex.getMessage());
        err.setRuta(req.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }
}
