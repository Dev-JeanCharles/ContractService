package com.service.contract_service.application.web.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.contract_service.application.web.config.security.interfaces.CorrelationIdProvider;
import com.service.contract_service.application.web.config.security.interfaces.ResponseBuilder;
import com.service.contract_service.domain.commons.UnauthorizationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestFilterHandler extends OncePerRequestFilter {

    private static final String TIMESTAMP = "timestamp";
    private static final String STATUS = "status";
    private static final String ERROR = "error";
    private static final String MESSAGE = "message";
    private static final String PATH = "path";
    public static final String CORRELATION_HEADER_NAME = "X-Correlation-ID";

    private final ObjectMapper mapper;
    private final ResponseBuilder responseBuilder;
    private final CorrelationIdProvider correlationIdProvider;

    public RequestFilterHandler(ObjectMapper mapper, ResponseBuilder responseBuilder, CorrelationIdProvider correlationIdProvider) {
        this.mapper = mapper;
        this.responseBuilder = responseBuilder;
        this.correlationIdProvider = correlationIdProvider;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) {
        String correlationId = correlationIdProvider.getCorrelationId(request);
        try {
            MutableHttpServletRequestWrapper requestWrapper = new MutableHttpServletRequestWrapper(request, correlationId);
            response.setHeader(CORRELATION_HEADER_NAME, correlationId);
            response.setContentType("application/json");
            filterChain.doFilter((ServletRequest) requestWrapper, response);
        } catch (UnauthorizationException | IOException | ServletException ex) {
            handleException(request, response, ex);
        }
    }

    public static class MutableHttpServletRequestWrapper extends HttpServletRequestWrapper {

        private final Map<String, String> headerMap = new HashMap<>();

        public MutableHttpServletRequestWrapper(HttpServletRequest request, String correlationId) {
            super(request);
            headerMap.put(CORRELATION_HEADER_NAME, correlationId);
        }

        @Override
        public String getHeader(String name) {
            return headerMap.getOrDefault(name, super.getHeader(name));
        }
    }

    @Component
    public static class DefaultResponseBuilder implements ResponseBuilder {
        @Override
        public Map<String, Object> build(HttpServletRequest request, HttpServletResponse response, Exception ex) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put(TIMESTAMP, OffsetDateTime.now(ZoneOffset.UTC));
            responseMap.put(STATUS, response.getStatus());
            responseMap.put(ERROR, HttpStatus.valueOf(response.getStatus()));
            responseMap.put(MESSAGE, ex.getMessage());
            responseMap.put(PATH, (request != null) ? request.getRequestURI() : "unknown");
            return responseMap;
        }
    }

    @Component
    public static class DefaultCorrelationIdProvider implements CorrelationIdProvider {
        @Override
        public String getCorrelationId(HttpServletRequest request) {
            String correlationHeader = request.getHeader(CORRELATION_HEADER_NAME);
            return (correlationHeader != null) ? correlationHeader : UUID.randomUUID().toString();
        }
    }

    private void handleException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        try {
            mapper.writeValue(response.getWriter(), responseBuilder.build(request, response, ex));
            response.getWriter().flush();
        } catch (IOException e) {
            throw new RuntimeException("Error writing response", e);
        }
    }

}
