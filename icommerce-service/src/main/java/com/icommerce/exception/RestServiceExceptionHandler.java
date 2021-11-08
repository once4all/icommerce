package com.icommerce.exception;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.StaleObjectStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.OptimisticLockException;
import javax.validation.ConstraintViolationException;
import java.util.Objects;
import java.util.stream.Collectors;

public class RestServiceExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestServiceExceptionHandler.class);


    public RestServiceExceptionHandler() {
    }

    @ExceptionHandler({Throwable.class})
    public ResponseEntity<RestErrorResponse> handleException(Throwable ex) {
        LOGGER.error("{}: {}", new Object[]{ex.getClass().getSimpleName(), ex.getMessage(), ex});
        HttpStatus code = HttpStatus.INTERNAL_SERVER_ERROR;
        String errorMessage;
        if (ex instanceof EntityNotFoundException) {
            code = HttpStatus.NOT_FOUND;
            errorMessage = "Entity not found.";
        } else if (!(ex instanceof StaleObjectStateException) && !(ex instanceof OptimisticLockException)) {
            if (ex instanceof AuthenticationException) {
                code = HttpStatus.UNAUTHORIZED;
                errorMessage = "Your account is disabled, please contact the administrator.";
            } else if (ex instanceof AccessDeniedException) {
                code = HttpStatus.FORBIDDEN;
                errorMessage = "You do not have permission to use this action. Please contact the site administrator to request access.";
            } else if (!(ex instanceof MethodArgumentNotValidException) && !(ex instanceof MethodArgumentTypeMismatchException)) {
                if (ex instanceof ConstraintViolationException) {
                    code = HttpStatus.BAD_REQUEST;
                    String errorFields = (String) ((ConstraintViolationException) ex).getConstraintViolations().stream().map((cv) -> {
                        if (Objects.isNull(cv)) {
                            return "null";
                        } else {
                            String path = cv.getPropertyPath().toString();
                            return StringUtils.substringAfterLast(path, ".") + ": " + cv.getMessage();
                        }
                    }).collect(Collectors.joining(", "));
                    errorMessage = "Error fields: " + errorFields;
                } else {
                    errorMessage = "An unexpected exception occurred. Please try again or send a support request to administrators.";
                }
            } else {
                code = HttpStatus.BAD_REQUEST;
                errorMessage = "An unexpected mandatory attributes.";
            }
        } else {
            code = HttpStatus.CONFLICT;
            errorMessage = "The object has been updated or deleted by another user. Please reload the page and try again.";
        }

        RestErrorResponse error = new RestErrorResponse(code.value());
        error.setError(errorMessage);
        return new ResponseEntity(error, code);
    }
}
