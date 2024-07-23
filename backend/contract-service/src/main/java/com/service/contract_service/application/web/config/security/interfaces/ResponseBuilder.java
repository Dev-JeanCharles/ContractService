package com.service.contract_service.application.web.config.security.interfaces;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public interface ResponseBuilder {
    Map<String, Object> build(HttpServletRequest request, HttpServletResponse response, Exception ex);
}