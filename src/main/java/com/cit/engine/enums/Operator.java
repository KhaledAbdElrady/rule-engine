package com.cit.engine.enums;

public enum Operator {
    EQUALS("=="),
    NOT_EQUALS("!="),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    GREATER_THAN_OR_EQUALS(">="),
    LESS_THAN_OR_EQUALS("<="),
    BEFORE("<"),
    AFTER(">");

    private final String spelOperator;

    Operator(String spelOperator) {
        this.spelOperator = spelOperator;
    }

    public String getSpelOperator() {
        return spelOperator;
    }
}
