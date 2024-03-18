package nl.alimeshkat.recipes.controller;

import lombok.extern.slf4j.Slf4j;
import nl.alimeshkat.recipes.model.RecipesErrorResponse;
import nl.alimeshkat.recipes.service.ResourceNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

import static nl.alimeshkat.recipes.controller.ErrorMessageMapper.DEFAULT_ERROR_MESSAGE;
import static nl.alimeshkat.recipes.controller.ErrorMessageMapper.toErrorMessage;

@Slf4j
@ControllerAdvice()
public class RecipesControllerAdvice {

    @NotNull
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RecipesErrorResponse> validationException(MethodArgumentNotValidException e) {
        log.error("error", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                toErrorMessage(e, "Incorrect fields:", ".")
        );
    }

    @NotNull
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<RecipesErrorResponse> resourceNotFoundException(ResourceNotFoundException e) {
        log.error("error", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                toErrorMessage(List.of(e.getMessage()), null, null)
        );
    }

    @NotNull
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RecipesErrorResponse> defaultExceptionHandler(Exception e) {
        log.error("error", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                DEFAULT_ERROR_MESSAGE
        );
    }
}
