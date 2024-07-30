package mfreitas.msemail.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(MailjetException.class)
    public ResponseEntity<StandardError> objectAlreadyExists(MailjetException ex, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND ).body(
            new StandardError(LocalDateTime.now(), ex.getMessage(), HttpStatus.NOT_FOUND.value(), request.getRequestURI())
        );
    }

    @ExceptionHandler(MailjetSocketTimeoutException.class)
    public ResponseEntity<StandardError> objectNotFound(MailjetSocketTimeoutException ex, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND ).body(
            new StandardError(LocalDateTime.now(), ex.getMessage(), HttpStatus.NOT_FOUND.value(), request.getRequestURI())
        );
    }
}
