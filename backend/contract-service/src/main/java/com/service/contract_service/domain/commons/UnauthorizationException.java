package com.service.contract_service.domain.commons;

public class UnauthorizationException extends RuntimeException{

    public UnauthorizationException(String message) {
        super(message);
    }
}
