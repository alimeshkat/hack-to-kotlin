package nl.rabobank.kotlinmovement.recipes.controller

import nl.rabobank.kotlinmovement.recipes.model.RecipesErrorResponse
import nl.rabobank.kotlinmovement.recipes.service.ResourceNotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class RecipesControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun validationException(e: MethodArgumentNotValidException): ResponseEntity<RecipesErrorResponse> {
        log.error("error", e)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorMessageMapper.toErrorMessage(e, "Incorrect fields:", ".")
        )
    }

    @ExceptionHandler(ResourceNotFoundException::class)
    fun resourceNotFoundException(e: ResourceNotFoundException): ResponseEntity<RecipesErrorResponse> {
        log.error("error", e)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ErrorMessageMapper.toErrorMessage(listOf(e.message))
        )
    }

    @ExceptionHandler(Exception::class)
    fun defaultExceptionHandler(e: Exception?): ResponseEntity<RecipesErrorResponse> {
        log.error("error", e)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ErrorMessageMapper.DEFAULT_ERROR_MESSAGE
        )
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(RecipesControllerAdvice::class.java)
    }
}
