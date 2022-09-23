package nl.rabobank.kotlinmovement.recipes.controller;

import lombok.extern.slf4j.Slf4j;
import nl.rabobank.kotlinmovement.recipes.model.RecipesErrorResponse;
import nl.rabobank.kotlinmovement.recipes.service.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

import static nl.rabobank.kotlinmovement.recipes.controller.ErrorMessageMapper.*;

@Slf4j
@ControllerAdvice()
public class RecipesControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RecipesErrorResponse> validationException(MethodArgumentNotValidException e) {
        log.error("error", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                toErrorMessage(e, "Incorrect fields:", ".")
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<RecipesErrorResponse> resourceNotFoundException(ResourceNotFoundException e) {
        log.error("error", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                toErrorMessage(List.of(e.getMessage()), null, null)
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RecipesErrorResponse> defaultExceptionHandler(Exception e) {
        log.error("error", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                DEFAULT_ERROR_MESSAGE
        );
    }
}
