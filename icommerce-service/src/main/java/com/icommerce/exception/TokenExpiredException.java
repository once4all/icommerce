package com.icommerce.exception;

import lombok.Getter;

@Getter
public class TokenExpiredException extends RuntimeException {
    private final String message;

    public TokenExpiredException(String message) {
        super(message);
        this.message = message;
    }
}
