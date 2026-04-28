package com.tribune.demo.km.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import org.thymeleaf.exceptions.TemplateInputException;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application.
 * Handles all exceptions thrown across all controllers and provides appropriate
 * error responses (JSON for API endpoints, HTML views for web pages).
 * Note: The @ResponseStatus annotation ensures correct HTTP status codes are returned
 * even though the view/response is technically successful (200 by default).
 */
@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    /**
     * Handle when path not found
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NoResourceFoundException.class})
    public String handleResourceNotFound(NoResourceFoundException e) {
        log.warn("Resource not found: {}", e.getMessage());
        return "error/404";
    }

    /**
     * Handle template not found
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({TemplateInputException.class})
    public String handleTemplateNotFound(TemplateInputException e) {
        log.warn("Template not found: {}", e.getMessage());
        return "error/404";
    }

    // ==================== 400 BAD REQUEST ====================

    /**
     * Handle number format and type mismatch errors (invalid request parameters)
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({NumberFormatException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<?> handleBadRequest(Exception e) {
        log.warn("Bad request - Invalid parameter: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createErrorResponse("Bad Request", "Invalid request parameters", 400));
    }


    // ==================== 403 FORBIDDEN ====================

    /**
     * Handle access denied / insufficient permissions
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<?> handleAccessDenied(AccessDeniedException e) {
        log.warn("Access denied: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(createErrorResponse("Access Denied",
                        "You don't have permission to access this resource", 403));
    }


    // ==================== 401 UNAUTHORIZED ====================

    /**
     * Handle authentication errors (invalid credentials, expired tokens, etc.)
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException e) {
        log.warn("Authentication failed: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(createErrorResponse("Authentication Failed",
                        "Please log in to continue", 401));
    }

    // ==================== 500 INTERNAL SERVER ERROR ====================

    /**
     * Handle database/data access errors
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({DataAccessException.class})
    public ResponseEntity<?> handleDatabaseError(DataAccessException e) {
        log.error("Database error occurred", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Database Error",
                        "An error occurred while accessing the database", 500));
    }

    /**
     * Global fallback handler for any unhandled exception
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handleGenericException(Exception e) {
        log.error("Unexpected error occurred", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Internal Server Error",
                        "An unexpected error occurred. Please try again later", 500));
    }


    // ==================== HELPER METHODS ====================

    /**
     * Create a structured error response for JSON APIs
     */
    private Map<String, Object> createErrorResponse(String error, String message, int status) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("success", false);
        errorMap.put("error", error);
        errorMap.put("message", message);
        errorMap.put("status", status);
        errorMap.put("timestamp", System.currentTimeMillis());
        return errorMap;
    }
}
