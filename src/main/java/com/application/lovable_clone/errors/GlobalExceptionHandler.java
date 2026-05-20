package com.application.lovable_clone.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequestException(BadRequestException exception)
    {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(apiError.httpStatus()).body(apiError);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException exception)
    {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, exception.getResourceName()+"not found with id "+exception.getResourceId());
        return ResponseEntity.status(apiError.httpStatus()).body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception)
    {

       List<ApiFieldError> errors = exception.getBindingResult()
               .getFieldErrors()
               .stream()
               .map(fieldError -> new ApiFieldError(fieldError.getField(),fieldError.getDefaultMessage()))
               .toList();
       ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,"Input Validation Failed",errors);
       return ResponseEntity.status(apiError.httpStatus()).body(apiError);
    }


}
