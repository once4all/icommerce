package com.icommerce.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

@Getter
@Setter
public class RestErrorResponse {
    private final Date timestamp;
    private final String path;
    private int status;
    private String error;
    private String correlationId;

    public RestErrorResponse(int status) {
        this.status = status;
        this.timestamp = new Date();
        this.path = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getRequestURI();
    }

}
