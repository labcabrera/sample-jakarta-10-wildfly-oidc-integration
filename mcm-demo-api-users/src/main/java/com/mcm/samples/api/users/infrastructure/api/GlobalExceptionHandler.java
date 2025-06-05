package com.mcm.samples.api.users.infrastructure.api;

import java.time.LocalDateTime;

import com.mcm.samples.api.users.domain.exception.UserNotFoundException;
import com.mcm.samples.api.users.generated.users.model.ApiError;

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
        log.error("Handling global exception", exception);
        Status status = mapStatus(exception);

        ApiError apiError = new ApiError();
        apiError.setCode(String.valueOf(status.getStatusCode()));
        apiError.setMessage(exception.getMessage());
        apiError.setTimestamp(LocalDateTime.now());

        return Response
            .status(status)
            .entity(apiError)
            .header("Content-Type", "application/json")
            .build();
    }

    private Status mapStatus(Throwable exception) {
        if (exception instanceof UserNotFoundException) {
            return Response.Status.NOT_FOUND;
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