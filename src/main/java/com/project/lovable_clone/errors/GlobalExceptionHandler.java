package com.project.lovable_clone.errors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequestException(BadRequestException exception)
    {
       ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,exception.getLocalizedMessage());
       log.error(String.valueOf(apiError),exception);
       return ResponseEntity.status(HttpStatus.BAD_REQUEST)
               .body(apiError);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException exception)
    {
        ApiError apiError = new ApiError
        (HttpStatus.NOT_FOUND,"Resource "+exception.getResourceName()+" not found with id "+exception.getResourceId());
        log.error(String.valueOf(apiError),exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception)
    {
        List<ApiFieldError> subErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ApiFieldError(error.getField(),error.getDefaultMessage()))
                .collect(Collectors.toList());

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,"Input Validation Exception",subErrors);
        log.error(String.valueOf(apiError),exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(apiError);
    }
}
