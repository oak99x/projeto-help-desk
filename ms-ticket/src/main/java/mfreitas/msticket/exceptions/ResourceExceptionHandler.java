package mfreitas.msticket.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ObjectAlreadyExistsException.class)
    public ResponseEntity<StandardError> objectAlreadyExists(ObjectAlreadyExistsException ex, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND ).body(
            new StandardError(LocalDateTime.now(), ex.getMessage(), HttpStatus.CONFLICT.value(), request.getRequestURI())
        );
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException ex, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND ).body(
            new StandardError(LocalDateTime.now(), ex.getMessage(), HttpStatus.NOT_FOUND.value(), request.getRequestURI())
        );
    }

    @ExceptionHandler(FailedToRespondException.class)
    public ResponseEntity<StandardError> objectNotFound(FailedToRespondException ex, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND ).body(
            new StandardError(LocalDateTime.now(), ex.getMessage(), HttpStatus.NOT_FOUND.value(), request.getRequestURI())
        );
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<StandardError> objectNotFound(FeignException ex, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND ).body(
            new StandardError(LocalDateTime.now(), ex.getMessage(), HttpStatus.NOT_FOUND.value(), request.getRequestURI())
        );
    }
}
