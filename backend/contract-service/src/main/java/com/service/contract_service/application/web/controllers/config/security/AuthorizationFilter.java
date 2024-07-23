package com.service.contract_service.application.web.controllers.config.security;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.service.contract_service.application.web.controllers.config.parser.JsonParserBuilder;
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

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

public class AuthorizationFilter extends OncePerRequestFilter {

    private final String serviceKey;
    private final List<AntPathRequestMatcher> ignoredPaths;

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

    private List<AntPathRequestMatcher> createIgnoredPaths() {
        List<AntPathRequestMatcher> matchers = new ArrayList<>();
        for (String path : IGNORE_AUTH_PATH_LIST) {
            matchers.add(new AntPathRequestMatcher(path));
        }
        return matchers;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response, @NonNull FilterChain filterChain) {
        if (verifyIgnoreAuthorization(request)) {
            try {
                filterChain.doFilter(request, response);
            } catch (Exception e) {
                throw new RuntimeException("Error during filter chain processing", e);
            }
            return;
        }

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.equals(this.serviceKey)) {
            SecurityContextHolder.getContext().setAuthentication(systemAuthorization());
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            try {
                JsonParserBuilder.defaultObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .writeValue(response.getWriter(), buildObject(request, response));
            } catch (Exception e) {
                throw new RuntimeException("Error writing response", e);
            } finally {
                try {
                    response.getWriter().flush();
                } catch (Exception e) {
                    throw new RuntimeException("Error flushing response writer", e);
                }
            }
            return;
        }

        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            throw new RuntimeException("Error during filter chain processing", e);
        }
    }

    private Map<String, Object> buildObject(HttpServletRequest req, HttpServletResponse res) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("timestamp", OffsetDateTime.now(ZoneOffset.UTC));
        responseMap.put("status", res.getStatus());
        responseMap.put("error", HttpStatus.valueOf(res.getStatus()));
        responseMap.put("path", req.getRequestURI());
        return responseMap;
    }

    private boolean verifyIgnoreAuthorization(HttpServletRequest request) {
        return ignoredPaths.stream().anyMatch(matcher -> matcher.matches(request));
    }

    private PreAuthenticatedAuthenticationToken systemAuthorization() {
        return new PreAuthenticatedAuthenticationToken(
                this.serviceKey,
                null,
                Collections.singletonList((GrantedAuthority) () -> ROLE_SYSTEM)
        );
    }
    public RequestMatcher[] getIgnoreAuthorizationPathList() {
        return ignoredPaths.toArray(new RequestMatcher[0]);
    }
}