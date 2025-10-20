/*package com.example.Config_service.exception;

import com.example.Config_service.api.PrefsController;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Order(Ordered.LOWEST_PRECEDENCE)
@RestControllerAdvice(basePackageClasses = { PrefsController.class })
public class ManejadorExcepcionesGlobal {

  @ExceptionHandler(ExcepcionNoEncontrado.class)
  public ResponseEntity<ErrorApi> noEncontrado(ExcepcionNoEncontrado ex, HttpServletRequest req) {
    ErrorApi err = new ErrorApi();
    err.setEstado(HttpStatus.NOT_FOUND.value());
    err.setError("No encontrado");
    err.setMensaje(ex.getMessage());
    err.setRuta(req.getRequestURI());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
  }

  @ExceptionHandler(ExcepcionSolicitudInvalida.class)
  public ResponseEntity<ErrorApi> solicitudInvalida(ExcepcionSolicitudInvalida ex, HttpServletRequest req) {
    ErrorApi err = new ErrorApi();
    err.setEstado(HttpStatus.BAD_REQUEST.value());
    err.setError("Solicitud inválida");
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
    err.setError("Validación");
    err.setMensaje(msg);
    err.setRuta(req.getRequestURI());
    return ResponseEntity.badRequest().body(err);
  }
  @ExceptionHandler(HttpMessageNotReadableException.class)
public ResponseEntity<ErrorApi> bodyRequerido(HttpMessageNotReadableException ex, HttpServletRequest req) {
  ErrorApi err = new ErrorApi();
  err.setEstado(HttpStatus.BAD_REQUEST.value());
  err.setError("Solicitud inválida");
  err.setMensaje("El formato esta mal corriguelo");
  err.setRuta(req.getRequestURI());
  return ResponseEntity.badRequest().body(err);
}

@ExceptionHandler(MethodArgumentTypeMismatchException.class)
public ResponseEntity<ErrorApi> tipoParametroInvalido(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
  String nombre = ex.getName();
  String esperado = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "valor válido";
  String msg;
  if ("Boolean".equalsIgnoreCase(esperado) || "boolean".equalsIgnoreCase(esperado)) {
    msg = nombre + " debe ser true o false";
  } else {
    msg = "Parámetro '" + nombre + "' con tipo inválido (se esperaba " + esperado + ")";
  }
  ErrorApi err = new ErrorApi();
  err.setEstado(HttpStatus.BAD_REQUEST.value());
  err.setError("Solicitud inválida");
  err.setMensaje(msg);
  err.setRuta(req.getRequestURI());
  return ResponseEntity.badRequest().body(err);
}

@ExceptionHandler(MissingServletRequestParameterException.class)
public ResponseEntity<ErrorApi> parametroFaltante(MissingServletRequestParameterException ex, HttpServletRequest req) {
  ErrorApi err = new ErrorApi();
  err.setEstado(HttpStatus.BAD_REQUEST.value());
  err.setError("Solicitud inválida");
  err.setMensaje("Falta el parámetro '" + ex.getParameterName() + "'");
  err.setRuta(req.getRequestURI());
  return ResponseEntity.badRequest().body(err);
}

}

/*POST sin body
POST http://localhost:8080/api/prefs/lucia → Body vacío → debe dar 400 con “Cmal formado”.

GET con strict inválido
GET http://localhost:8080/api/prefs/lucia?strict=aaa → 400 con “strict debe ser true o false”.

GET usuario inexistente
GET http://localhost:8080/api/prefs/ghost → 404 con “usuario no existe”. */