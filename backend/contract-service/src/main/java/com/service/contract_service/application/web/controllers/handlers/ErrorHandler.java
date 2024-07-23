package com.service.contract_service.application.web.controllers.handlers;

import com.service.contract_service.application.web.controllers.ErrorField;
import com.service.contract_service.application.web.controllers.dto.responses.ErrorResponse;
import com.service.contract_service.domain.commons.UnauthorizationException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ErrorField> errorFields = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ErrorField(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        ErrorResponse errorResponse = new ErrorResponse("Invalid request parameters", errorFields);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        logger.error("[EXCEPTION]-[ErrorHandler] DataIntegrityViolationException: ", ex);
        ErrorResponse errorResponse = new ErrorResponse("Data integrity violation", List.of(new ErrorField("data", ex.getMessage())));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        List<ErrorField> errorFields = ex.getConstraintViolations().stream()
                .map(violation -> new ErrorField(violation.getPropertyPath().toString(), violation.getMessage()))
                .collect(Collectors.toList());

        ErrorResponse errorResponse = new ErrorResponse("Validation error", errorFields);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizationException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizationException(UnauthorizationException ex) {
        logger.error("[EXCEPTION]-[ErrorHandler] UnauthorizationException: ", ex);

        List<ErrorField> errorFields = List.of(new ErrorField("error", ex.getMessage()));
        ErrorResponse errorResponse = new ErrorResponse("Unauthorized access", errorFields);

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
}
