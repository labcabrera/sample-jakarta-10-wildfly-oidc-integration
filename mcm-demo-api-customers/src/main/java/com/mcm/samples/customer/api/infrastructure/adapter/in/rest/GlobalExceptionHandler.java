package com.mcm.samples.customer.api.infrastructure.adapter.in.rest;

import java.time.LocalDateTime;
import java.util.Map;

import com.mcm.samples.customer.api.domain.exception.CustomerNotFoundException;
import com.mcm.samples.customer.api.domain.exception.CustomerNotModifiedException;
import com.mcm.samples.customer.api.domain.exception.SearchExpressionParseException;
import com.mcm.samples.customer.api.domain.model.ApiError;

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

    private static Map<Class<? extends Exception>, Status> statusMap = Map.of(
        CustomerNotFoundException.class, Response.Status.NOT_FOUND,
        CustomerNotModifiedException.class, Response.Status.BAD_REQUEST,
        SearchExpressionParseException.class, Response.Status.BAD_REQUEST,
        ForbiddenException.class, Response.Status.FORBIDDEN,
        ConstraintViolationException.class, Response.Status.BAD_REQUEST,
        IllegalArgumentException.class, Response.Status.BAD_REQUEST,
        NullPointerException.class, Response.Status.INTERNAL_SERVER_ERROR,
        IllegalStateException.class, Response.Status.INTERNAL_SERVER_ERROR);

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
        for (Map.Entry<Class<? extends Exception>, Status> entry : statusMap.entrySet()) {
            if (entry.getKey().isInstance(exception)) {
                return entry.getValue();
            }
        }
        return Response.Status.INTERNAL_SERVER_ERROR;
    }

}