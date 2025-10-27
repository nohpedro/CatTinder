package com.example.cattinder.users.exception;

import com.example.cattinder.users.preference.exception.PreferenceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice // <- garantiza JSON en todas las respuestas de error
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ---------------------------
    // 404 - Recurso no encontrado
    // ---------------------------

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        logger.error("Usuario no encontrado", ex);
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(PreferenceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePreferenceNotFound(PreferenceNotFoundException ex) {
        logger.error("Preferencia no encontrada", ex);
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // ---------------------------
    // 400 - Error de validación de @Valid en el body (DTOs)
    // ---------------------------

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {

        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));

        logger.warn("Error de validación en request body: {}", errors);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Errores de validación: " + errors);
    }

    // ---------------------------
    // 400 - Error de validación en parámetros (PathVariable / RequestParam)
    // ---------------------------

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {

        String errors = ex.getConstraintViolations()
                .stream()
                .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                .collect(Collectors.joining("; "));

        logger.warn("Error de validación en path/query params: {}", errors);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Errores de validación: " + errors);
    }

    // ---------------------------
    // 400 - Violaciones de integridad de BD
    // (por ejemplo: email duplicado, FK inválida, nulls prohibidos por @Column(nullable=false), etc.)
    // ---------------------------

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {

        logger.warn("Violación de integridad de datos", ex);

        // Mensaje más humano para el cliente
        String message = "Violación de integridad de datos. " +
                "Posiblemente estás enviando información duplicada o inválida (por ejemplo, email ya registrado).";

        return buildErrorResponse(HttpStatus.BAD_REQUEST, message);
    }

    // ---------------------------
    // 500 - Cualquier otra excepción no controlada
    // ---------------------------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        logger.error("Error inesperado", ex);
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error inesperado en el servidor"
        );
    }

    // ---------------------------
    // Utilidad para construir respuesta estándar
    // ---------------------------

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message) {
        ErrorResponse error = new ErrorResponse(
                status.value(),
                message,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, status);
    }

    // ---------------------------
    // DTO de error para respuestas JSON
    // ---------------------------

    public static class ErrorResponse {
        private final int status;
        private final String message;
        private final LocalDateTime timestamp;

        public ErrorResponse(int status, String message, LocalDateTime timestamp) {
            this.status = status;
            this.message = message;
            this.timestamp = timestamp;
        }

        public int getStatus() { return status; }
        public String getMessage() { return message; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }
}
