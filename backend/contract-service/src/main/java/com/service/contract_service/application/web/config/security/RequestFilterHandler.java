package com.service.contract_service.application.web.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.contract_service.domain.commons.UnauthorizationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jdk.jfr.ContentType;
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
    private static final String CORRELATION_HEADER_NAME = "X-Correlation-ID";

    private final ObjectMapper mapper;

    public RequestFilterHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) {
        String correlationId = retrieveCorrelation(request);
        handleAndFilterServletRequest(request, response, filterChain, correlationId);
    }

    private String retrieveCorrelation(HttpServletRequest request) {
        String correlationHeader = request.getHeader(CORRELATION_HEADER_NAME);
        return (correlationHeader != null) ? correlationHeader : UUID.randomUUID().toString();
    }

    private void handleAndFilterServletRequest(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain,
            String correlationId
    ) {
        try {
            MutableHttpServletRequestWrapper requestWrapper = new MutableHttpServletRequestWrapper(request);
            requestWrapper.addHeader(CORRELATION_HEADER_NAME, correlationId);
            response.setHeader(CORRELATION_HEADER_NAME, correlationId);
            response.setContentType("application/json");

            filterChain.doFilter((ServletRequest) requestWrapper, response);
        } catch (UnauthorizationException | IOException | ServletException ex) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            try {
                mapper.writeValue(response.getWriter(), buildObject(request, response, ex));
                response.getWriter().flush();
            } catch (IOException e) {
                throw new RuntimeException("Error writing response", e);
            }
        }
    }

    private Map<String, Object> buildObject(
            HttpServletRequest req,
            HttpServletResponse res,
            Exception ex
    ) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put(TIMESTAMP, OffsetDateTime.now(ZoneOffset.UTC));
        responseMap.put(STATUS, res.getStatus());
        responseMap.put(ERROR, HttpStatus.valueOf(res.getStatus()));
        responseMap.put(MESSAGE, ex.getMessage());
        responseMap.put(PATH, req.getRequestURI());
        return responseMap;
    }

    public static class MutableHttpServletRequestWrapper extends HttpServletRequestWrapper {

        private final Map<String, String> headerMap = new HashMap<>();

        public MutableHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        public void addHeader(String name, String value) {
            if (super.getHeader(name) == null || super.getHeader(name).isBlank()) {
                headerMap.put(name, value);
            }
        }

        @Override
        public String getHeader(String name) {
            return headerMap.getOrDefault(name, super.getHeader(name));
        }
    }
}
