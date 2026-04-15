package org.coffeeshop.Exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.coffeeshop.Exceptions.UserExceptions.CustomerServiceException;
import org.coffeeshop.Exceptions.UserExceptions.StaffServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;


/**
 * this is the global exceptin handler for the whistlestop coffee shop application
 * it is responsible for handling exceptions thrown by the servce and controller layers
*/
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * this method helps to build a standard error response body with a given HTTP status and message.
     * It creates a map containing the error message and returns it wrapped in a ResponseEntity with
     * the specified HTTP status. This is used by the various exception handlers to ensure consistent error responses across the application.
     */
    private ResponseEntity<Map<String, String>> buildErrorResponse(HttpStatus status, String message) {
        Map<String, String> body = new HashMap<>();
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }

    /**
     * Handles exceptions thrown by service classes (CustomerServiceException, StaffServiceException).
     * If there is an underlying cause, it is treated as an internal server error (500).
     * Otherwise, it is treated as a bad request (400).
     * @return  a response entity that contains the error message and appropriate HTTP status code
     */
    @ExceptionHandler({CustomerServiceException.class, StaffServiceException.class})
    public ResponseEntity<Map<String, String>> handleServiceException(RuntimeException ex) {
        // If there is an underlying cause, this is likely a persistence/DB error → 500
        if (ex.getCause() != null) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occurred");
        }
        // Otherwise treat as a bad-request / business-logic failure → 400
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * Handles validation exceptions that occur when method arguments fail validation checks.
     * It extracts field-specific error messages and returns them in a structured format with a 400 status code.
     * @return a response entity containing the validation errors and a 400 status code
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> fieldErrors.put(e.getField(), e.getDefaultMessage()));
        Map<String, Object> body = new HashMap<>();
        body.put("errors", fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Handles IllegalArgumentException, typically thrown for invalid method arguments.
     * Returns a 400 Bad Request response with the exception message.
     * @return a response entity that contains the appropriate error message and a 400 status code
     */

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * Handles SecurityException, which may be thrown for unauthorized access attempts.
     * Returns a 401 Unauthorized response with the exception message.
     */
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorized(SecurityException ex) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    /**
     * Handles EntityNotFoundException and NoSuchElementException, which indicate that a requested resource was not found.
     * Returns a 404 Not Found response with the exception message.
     */
     @ExceptionHandler({EntityNotFoundException.class, NoSuchElementException.class})
    public ResponseEntity<Map<String, String>> handleNotFound(RuntimeException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }
}
