package nl.rabobank.kotlinmovement.recipes.controller;

import nl.rabobank.kotlinmovement.recipes.model.RecipesErrorResponse;
import nl.rabobank.kotlinmovement.recipes.service.ResourceNotFoundException;
import nl.rabobank.kotlinmovement.recipes.util.ErrorMessageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@ControllerAdvice()
public class RecipesControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RecipesErrorResponse> validationException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new RecipesErrorResponse(
                        ErrorMessageUtil.toErrorMessage(e, "Incorrect fields:", ".")
                )
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<RecipesErrorResponse> resourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new RecipesErrorResponse(
                        ErrorMessageUtil.toErrorMessage(List.of(e.getMessage()), null, null)
                )

        );
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RecipesErrorResponse> defaultExceptionHandler(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new RecipesErrorResponse(
                        ErrorMessageUtil.DEFAULT_ERROR_MESSAGE
                )
        );
    }
}
