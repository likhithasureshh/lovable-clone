package com.application.lovable_clone.errors;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
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

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNameNotFoundException(UsernameNotFoundException exception)
    {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND,"Username not found with userId: "+exception.getMessage());
        return ResponseEntity.status(apiError.httpStatus()).body(apiError);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleUserNameNotFoundException(AuthenticationException exception)
    {
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN,"Authentication failed : "+exception.getMessage());
        return ResponseEntity.status(apiError.httpStatus()).body(apiError);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiError> handleUserNameNotFoundException(JwtException exception)
    {
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN,"Invalid Jwt Token  : "+exception.getMessage());
        return ResponseEntity.status(apiError.httpStatus()).body(apiError);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleUserNameNotFoundException(AccessDeniedException exception)
    {
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN,"Access denied  : "+exception.getMessage());
        return ResponseEntity.status(apiError.httpStatus()).body(apiError);
    }


}
