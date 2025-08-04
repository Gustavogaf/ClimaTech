package ifs.climatech.controller.exception;

import ifs.climatech.service.exception.RegraDeNegocioException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<Object> handleRegraDeNegocioException(RegraDeNegocioException ex) {
        return new ResponseEntity<>(Map.of("erro", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(Map.of("erro", ex.getMessage()), HttpStatus.NOT_FOUND);
    }
}
