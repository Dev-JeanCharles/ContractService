package com.service.contract_service.application.web.config.security;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.service.contract_service.application.web.config.parser.JsonParserBuilder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

public class AuthorizationFilter extends OncePerRequestFilter {

    private final String serviceKey;
    private final List<RequestMatcher> ignoredPaths;
    private static final String ROLE_SYSTEM = "ROLE_SYSTEM";
    private static final List<String> IGNORE_AUTH_PATH_LIST = Arrays.asList(
            "/health-check",
            "/health-check/**",
            "/healthcheck",
            "/healthcheck/**",
            "/metrics",
            "/metrics/**",
            "/actuator",
            "/actuator/**",
            "/error",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/webjars/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/docs",
            "/info",
            "/cache",
            "/favicon.ico",
            "/swagger.yaml",
            "/csrf"
    );

    public AuthorizationFilter(String serviceKey) {
        this.serviceKey = serviceKey;
        this.ignoredPaths = createIgnoredPaths();
    }

    private List<RequestMatcher> createIgnoredPaths() {
        List<RequestMatcher> matchers = new ArrayList<>();
        for (String path : IGNORE_AUTH_PATH_LIST) {
            matchers.add(new AntPathRequestMatcher(path));
        }
        return matchers;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response, @NonNull FilterChain filterChain) {

        try {
            if (shouldIgnoreAuthorization(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            if (isAuthorized(request)) {
                SecurityContextHolder.getContext().setAuthentication(createSystemAuthorization());
            } else {
                handleUnauthorized(response, request);
                return;
            }
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            throw new RuntimeException("Error during filter chain processing", e);
        }
    }

    private boolean shouldIgnoreAuthorization(HttpServletRequest request) {
        return ignoredPaths.stream().anyMatch(matcher -> matcher.matches(request));
    }

    private boolean isAuthorized(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        return authorizationHeader != null && authorizationHeader.equals(this.serviceKey);
    }

    private void handleUnauthorized(HttpServletResponse response, HttpServletRequest request) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        Map<String, Object> errorResponse = buildErrorResponse(request, response);
        JsonParserBuilder.getObjectMapper()
                .registerModule(new JavaTimeModule())
                .writeValue(response.getWriter(), errorResponse);
        response.getWriter().flush();
    }

    private Map<String, Object> buildErrorResponse(HttpServletRequest req, HttpServletResponse res) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("timestamp", OffsetDateTime.now(ZoneOffset.UTC));
        responseMap.put("status", res.getStatus());
        responseMap.put("error", HttpStatus.valueOf(res.getStatus()).getReasonPhrase());
        responseMap.put("path", req.getRequestURI());
        return responseMap;
    }

    private PreAuthenticatedAuthenticationToken createSystemAuthorization() {
        return new PreAuthenticatedAuthenticationToken(
                this.serviceKey,
                null,
                Collections.singletonList((GrantedAuthority) () -> ROLE_SYSTEM)
        );
    }

    public RequestMatcher[] getIgnoredPaths() {
        return ignoredPaths.toArray(new RequestMatcher[0]);
    }
}