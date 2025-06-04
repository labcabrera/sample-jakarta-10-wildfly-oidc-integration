package com.mcm.samples.rest.client.infrastructure.api;

import java.time.LocalDateTime;

import com.mcm.samples.rest.client.domain.entity.ApiError;
import com.mcm.samples.rest.client.domain.exception.ParseException;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Provider
@Slf4j
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        log.error("Global exception", exception);
        Status status = mapStatus(exception);
        ApiError errorInfo = ApiError.builder()
            .code(String.valueOf(status.getStatusCode()))
            .message(exception.getMessage())
            .timestamp(LocalDateTime.now())
            .build();
        return Response.status(status)
            .entity(errorInfo)
            .build();
    }

    private Status mapStatus(Throwable exception) {
        if (exception instanceof ParseException) {
            return Response.Status.BAD_REQUEST;
        }
        else if (exception instanceof ForbiddenException) {
            return Response.Status.FORBIDDEN;
        }
        else if (exception instanceof ConstraintViolationException) {
            return Response.Status.BAD_REQUEST;
        }
        else if (exception instanceof IllegalArgumentException) {
            return Response.Status.BAD_REQUEST;
        }
        else if (exception instanceof NullPointerException) {
            return Response.Status.INTERNAL_SERVER_ERROR;
        }
        else if (exception instanceof IllegalStateException) {
            return Response.Status.INTERNAL_SERVER_ERROR;
        }
        return Response.Status.INTERNAL_SERVER_ERROR;
    }

}