package com.service.contract_service.application.web.config.security.interfaces;

import jakarta.servlet.http.HttpServletRequest;

public interface CorrelationIdProvider {
    String getCorrelationId(HttpServletRequest request);
}