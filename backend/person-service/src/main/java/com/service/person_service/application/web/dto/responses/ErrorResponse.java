package com.service.person_service.application.web.dto.responses;

import com.service.person_service.application.web.controllers.ErrorField;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private List<ErrorField> errors;
}
