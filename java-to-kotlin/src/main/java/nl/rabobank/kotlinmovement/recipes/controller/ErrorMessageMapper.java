package nl.rabobank.kotlinmovement.recipes.controller;

import nl.rabobank.kotlinmovement.recipes.model.RecipesErrorResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.stream.Collectors;

public class ErrorMessageMapper {
    public static final RecipesErrorResponse DEFAULT_ERROR_MESSAGE =  new RecipesErrorResponse("Oops something went wrong");

    @NotNull
    public static RecipesErrorResponse toErrorMessage(MethodArgumentNotValidException methodArgumentNotValidException, String messagePrefix, String messagePostfix) {
        var messages = methodArgumentNotValidException.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        if (messages.size() == 0) {
            return DEFAULT_ERROR_MESSAGE;
        } else {
            return toErrorMessage(messages, messagePrefix, messagePostfix);
        }
    }

    @NotNull
    public static RecipesErrorResponse toErrorMessage(List<String> messages, String messagePrefix, String messagePostfix) {
        if (messages == null) {
            return DEFAULT_ERROR_MESSAGE;
        }

        var orderedMessage = messages.stream().sorted().collect(Collectors.toList());

        if (hasOneElement(orderedMessage)) {
            return new RecipesErrorResponse(getSingleErrorMessage(messagePrefix, messagePostfix, orderedMessage.get(0)));
        } else {
            return new RecipesErrorResponse(getMultiErrorMessage(messagePrefix, messagePostfix, orderedMessage));
        }
    }

    @NotNull
    private static String getSingleErrorMessage(String messagePrefix, String messagePostfix, String orderedMessage) {
        var stringBuilder = new StringBuilder();

        if (messagePrefix != null) {
            stringBuilder.append(messagePrefix);
        }
        stringBuilder.append(orderedMessage);
        if (messagePostfix != null) {
            stringBuilder.append(messagePostfix);
        }
        return stringBuilder.toString();
    }

    @NotNull
    private static String getMultiErrorMessage(String messagePrefix, String messagePostfix, List<String> orderedMessage) {

        var stringBuilder = new StringBuilder();

        for (int i = 0; orderedMessage.size() > i; i++) {
            final String next = orderedMessage.get(i);
            if (firstElement(i)) {
                if (messagePrefix != null) {
                    stringBuilder.append(messagePrefix);
                }
                stringBuilder.append(next);
                stringBuilder.append(",");
            } else if (lastElement(orderedMessage, i)) {
                stringBuilder.append(next);
                if (messagePostfix != null) {
                    stringBuilder.append(messagePostfix);
                }
            } else {
                stringBuilder.append(next);
                stringBuilder.append(",");
            }
        }
        return stringBuilder.toString();
    }

    @NotNull
    private static Boolean lastElement(List<String> orderedMessage, int i) {
        return orderedMessage.size() == i + 1;
    }

    @NotNull
    private static Boolean firstElement(int i) {
        return i == 0;
    }

    @NotNull
    private static Boolean hasOneElement(List<String> orderedMessage) {
        return orderedMessage.size() == 1;
    }

}
