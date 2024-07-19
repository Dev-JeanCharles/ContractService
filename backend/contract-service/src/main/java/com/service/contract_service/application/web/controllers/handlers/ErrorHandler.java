package com.service.contract_service.application.web.controllers.handlers;

import com.service.contract_service.application.web.controllers.dto.responses.ErrorField;
import com.service.contract_service.application.web.controllers.dto.responses.ErrorResponse;
import com.service.contract_service.domain.commons.DatabaseConnectionException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ErrorHandler {

    private static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    @ExceptionHandler(DatabaseConnectionException.class)
    public ResponseEntity<ErrorResponse> handleDatabaseConnectionException(DatabaseConnectionException ex) {
        logger.error("[EXCEPTION]-[ErrorHandler] DatabaseConnectionException: ", ex);
        ErrorResponse errorResponse = new ErrorResponse("Database connection error", List.of(new ErrorField("database", ex.getMessage())));
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ErrorField> errorFields = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ErrorField(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        ErrorResponse errorResponse = new ErrorResponse("Invalid request parameters", errorFields);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    private ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        logger.error("[EXCEPTION]-[ErrorHandler] DataIntegrityViolationException: ", ex);
        ErrorResponse errorResponse = new ErrorResponse("Data integrity violation", List.of(new ErrorField("data", ex.getMessage())));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    private ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        List<ErrorField> errorFields = ex.getConstraintViolations().stream()
                .map(violation -> new ErrorField(violation.getPropertyPath().toString(), violation.getMessage()))
                .collect(Collectors.toList());

        ErrorResponse errorResponse = new ErrorResponse("Validation error", errorFields);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
