package nl.rabobank.kotlinmovement.recipes.controller

import nl.rabobank.kotlinmovement.recipes.model.RecipesErrorResponse
import org.springframework.validation.ObjectError
import org.springframework.web.bind.support.WebExchangeBindException

object ErrorMessageMapper {
    val DEFAULT_ERROR_MESSAGE = RecipesErrorResponse("Oops something went wrong")
    fun toErrorMessage(
        methodArgumentNotValidException: WebExchangeBindException,
        messagePrefix: String?,
        messagePostfix: String?
    ): RecipesErrorResponse {
        val messages = methodArgumentNotValidException.bindingResult
            .allErrors
            .map { obj: ObjectError -> obj.defaultMessage }
        return if (messages.isEmpty()) {
            DEFAULT_ERROR_MESSAGE
        } else {
            toErrorMessage(messages, messagePrefix, messagePostfix)
        }
    }

    fun toErrorMessage(
        messages: List<String?>,
        messagePrefix: String? = null,
        messagePostfix: String? = null
    ): RecipesErrorResponse = messages.filterNotNull().sorted().let {
        RecipesErrorResponse(
            getMultiErrorMessage(
                messagePrefix,
                messagePostfix,
                it
            )
        )
    }

    private fun getMultiErrorMessage(
        messagePrefix: String?,
        messagePostfix: String?,
        orderedMessage: List<String>
    ): String =
        orderedMessage.joinToString(separator = ",", prefix = messagePrefix ?: "", postfix = messagePostfix ?: "")
}
