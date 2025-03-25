package com.cit.engine.exception.handler;

import com.cit.engine.exception.RuleNotFoundException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<Map<String, String>> handleInvalidEnumException(InvalidFormatException ex) {
        Map<String, String> error = new HashMap<>();

        // Check if the exception is caused by an invalid enum value
        if (ex.getTargetType().isEnum()) {
            error.put("error", "Invalid value for field. Allowed values: " + getAllowedEnumValues(ex.getTargetType()));
        } else {
            error.put("error", "Invalid input format.");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    private String getAllowedEnumValues(Class<?> enumType) {
        Object[] enumConstants = enumType.getEnumConstants();
        if (enumConstants == null) return "[]";
        StringBuilder allowedValues = new StringBuilder("[");
        for (Object constant : enumConstants) {
            allowedValues.append(constant.toString()).append(", ");
        }
        return allowedValues.substring(0, allowedValues.length() - 2) + "]";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid input");
        error.put("details", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(RuleNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleRuleNotFoundException(RuleNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Rule not found");
        error.put("details", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Internal server error");
        error.put("details", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
