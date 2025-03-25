package com.cit.engine.service.helper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class ValidationHelper {

    public static void validateField(String field, Class<?> targetClass) {
        Objects.requireNonNull(field, "Field name cannot be null");
        Objects.requireNonNull(targetClass, "Target class cannot be null");

        try {
            targetClass.getDeclaredField(field);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Invalid field name: " + field + " for class " + targetClass.getSimpleName(), e);
        }
    }

    public static Boolean parseBoolean(String value) {
        Objects.requireNonNull(value, "Boolean value cannot be null");
        if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
            throw new IllegalArgumentException("Invalid boolean value: " + value + ". Must be 'true' or 'false'");
        }
        return Boolean.parseBoolean(value);
    }

    public static Integer parseInteger(String value) {
        Objects.requireNonNull(value, "Integer value cannot be null");
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid integer value: " + value, e);
        }
    }

    public static Double parseDouble(String value) {
        Objects.requireNonNull(value, "Double value cannot be null");
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid double value: " + value, e);
        }
    }

    public static Long parseLong(String value) {
        Objects.requireNonNull(value, "Long value cannot be null");
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid long value: " + value, e);
        }
    }

    public static LocalDate parseLocalDate(String value) {
        Objects.requireNonNull(value, "LocalDate value cannot be null");
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid LocalDate value: " + value + ". Expected format: yyyy-MM-dd", e);
        }
    }

    public static LocalDateTime parseLocalDateTime(String value) {
        Objects.requireNonNull(value, "LocalDateTime value cannot be null");
        try {
            return LocalDateTime.parse(value);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid LocalDateTime value: " + value +
                    ". Expected format: yyyy-MM-ddTHH:mm:ss", e);
        }
    }
}