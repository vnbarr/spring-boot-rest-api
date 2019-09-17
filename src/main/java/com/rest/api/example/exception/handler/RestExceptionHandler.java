package com.rest.api.example.exception.handler;

import com.rest.api.example.exception.DuplicatedEntityException;
import com.rest.api.example.exception.EntityNotFoundException;
import com.rest.api.example.exception.InvalidInputException;
import com.rest.api.example.exception.MissingParameterException;
import com.rest.api.example.utils.ConfigurationLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.*;

/**
 * Created by vbarros on 16/09/2019 .
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        LOGGER.error("Not found", ex);
        ApiError apiError = new ApiError(NOT_FOUND, "Entity not found", ex.getEntity(), "104", ex);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(InvalidInputException.class)
    protected ResponseEntity<Object> handleValidationsException(InvalidInputException ex) {
        LOGGER.error("Missing parameter", ex);
        ApiError apiError = new ApiError(BAD_REQUEST, ex.getMessage(), ex.getKeyWord(), "103", ex);
        return buildResponseEntity(apiError);
    }

    /**
     * Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
     *
     * @param ex the ConstraintViolationException
     * @return the ApiError object
     */
    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(javax.validation.ConstraintViolationException ex) {
        LOGGER.error("Constraint Violation", ex);
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage("Validation error");
        apiError.addValidationErrors(ex.getConstraintViolations());
        return buildResponseEntity(apiError);
    }

    /**
     * Handle javax.persistence.EntityNotFoundException
     */
    @ExceptionHandler(javax.persistence.EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(javax.persistence.EntityNotFoundException ex) {
        LOGGER.error("Persistence Entity Not Found", ex);
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, ex));
    }

    /**
     * Handle DataIntegrityViolationException, inspects the cause for different DB causes.
     *
     * @param ex the DataIntegrityViolationException
     * @return the ApiError object
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex,
                                                                  WebRequest request) {
        LOGGER.error("DataIntegrity Violation", ex);
        if (ex.getCause() instanceof ConstraintViolationException) {
            return buildResponseEntity(new ApiError(HttpStatus.CONFLICT, "Database error", ex.getCause()));
        }
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex));
    }

    @ExceptionHandler(MissingParameterException.class)
    protected ResponseEntity<Object> handleMissingParamaterException(MissingParameterException ex) {
        LOGGER.error("Missing parameter", ex);
        String internalCode = ConfigurationLoader.getPropValue("MISSING_PARAMETER_ERROR_CODE");
        ApiError apiError = new ApiError(BAD_REQUEST, "Missing field: " + ex.getField(), ex.getEntity(), internalCode, ex);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(DuplicatedEntityException.class)
    protected ResponseEntity<Object> handleDuplicatedEntityException(DuplicatedEntityException ex) {
        LOGGER.error("Duplicated entity", ex);
        String internalCode = ConfigurationLoader.getPropValue("DATA_CONFLICTS_PARAMETER_ERROR_CODE");
        ApiError apiError = new ApiError(CONFLICT, "Duplicated entity", ex.getEntity(), internalCode, ex);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleUnknownException(Exception ex) {
        LOGGER.error("Unknown", ex);
        ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR, ex);
        return buildResponseEntity(apiError);
    }

}