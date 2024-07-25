package com.service.person_service.application.web.handlers;

import com.service.person_service.application.web.controllers.ErrorField;
import com.service.person_service.application.web.dto.responses.ErrorResponse;
import com.service.person_service.domain.commons.PersonNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ErrorField> errorFields = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ErrorField(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        log.error("[EXCEPTION]-[ErrorHandler] MethodArgumentNotValidException: ", ex);
        ErrorResponse errorResponse = new ErrorResponse("Invalid request parameters", errorFields);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("[EXCEPTION]-[ErrorHandler] DataIntegrityViolationException: ", ex);
        ErrorResponse errorResponse = new ErrorResponse("Data integrity violation", List.of(new ErrorField("data", ex.getMessage())));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePersonNotFoundException(PersonNotFoundException ex) {
        log.error("[EXCEPTION]-[ErrorHandler] PersonNotFoundException: ", ex);
        ErrorResponse errorResponse = new ErrorResponse("Person Not Found Exception", List.of(new ErrorField("person", ex.getMessage())));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
