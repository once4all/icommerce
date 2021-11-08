package com.icommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.ws.rs.ForbiddenException;

/**
 * Handle response status when occurring exception globally
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends RestServiceExceptionHandler {


    @ExceptionHandler({IllegalArgumentException.class,BadRequestException.class})
    public ResponseEntity<String> handleBadRequestException(final Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({ForbiddenException.class})
    public ResponseEntity<String> handleForbiddenException(final Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(value = NotFoundException.class)
    protected ResponseEntity<Object> handleNotFound() {
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<RestErrorResponse> handleException(Throwable ex) {
        return super.handleException(ex);
    }
}
