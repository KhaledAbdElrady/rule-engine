package com.cit.engine.service.helper;

import com.cit.engine.enums.Operator;
import com.cit.engine.model.PaymentTransaction;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class SpELExpressionBuilder {

    private static final Map<Class<?>, Function<String, Object>> PARSERS = Map.of(
            String.class, v -> v,
            Boolean.class, ValidationHelper::parseBoolean,
            Integer.class, ValidationHelper::parseInteger,
            Double.class, ValidationHelper::parseDouble,
            Long.class, ValidationHelper::parseLong,
            LocalDate.class, ValidationHelper::parseLocalDate,
            LocalDateTime.class, ValidationHelper::parseLocalDateTime
    );

    private static final Map<Class<?>, Function<Object, String>> VALUE_FORMATTERS = Map.of(
            String.class, value -> "'" + value + "'",
            Boolean.class, Object::toString,
            Integer.class, Object::toString,
            Double.class, Object::toString,
            Long.class, Object::toString,
            LocalDate.class, value -> "T(java.time.LocalDate).parse('" + value + "')",
            LocalDateTime.class, value -> "T(java.time.LocalDateTime).parse('" + value + "')"
    );

    private static final Map<String, Class<?>> FIELD_TYPE_CACHE = new ConcurrentHashMap<>();

    static {
        for (Field field : PaymentTransaction.class.getDeclaredFields()) {
            FIELD_TYPE_CACHE.put(field.getName(), field.getType());
        }
    }

    public static String buildConditionExpression(String field, Operator operator, String conditionValue) {
        ValidationHelper.validateField(field, PaymentTransaction.class);
        return "#transaction." + field + " " + operator.getSpelOperator() + " " + formatValue(field, conditionValue);
    }

    public static String buildActionExpression(String field, String newValue) {
        ValidationHelper.validateField(field, PaymentTransaction.class);
        return "#transaction." + field + " = " + formatValue(field, newValue);
    }

    private static String formatValue(String field, String value) {
        Objects.requireNonNull(value, "Value cannot be null");
        Class<?> fieldType = getFieldType(field);
        Object parsedValue = PARSERS.getOrDefault(fieldType, Objects::toString).apply(value);
        return VALUE_FORMATTERS.getOrDefault(fieldType, Object::toString).apply(parsedValue);
    }

    private static Class<?> getFieldType(String field) {
        return FIELD_TYPE_CACHE.computeIfAbsent(field, f -> {
            try {
                return PaymentTransaction.class.getDeclaredField(f).getType();
            } catch (NoSuchFieldException e) {
                throw new IllegalArgumentException("Invalid field name: " + field, e);
            }
        });
    }
}