package com.service.contract_service.application.web.controllers.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorField {
    private String field;
    private String errorMessage;
}
