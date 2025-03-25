package com.cit.engine.model;

import com.cit.engine.enums.Operator;
import com.cit.engine.service.helper.SpELExpressionBuilder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Condition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fieldName;

    @Enumerated(EnumType.STRING)
    private Operator operator;

    private String conditionValue;

    @Column(length = 1000)
    private String expression;

    public void generateExpression() {
        this.expression = SpELExpressionBuilder.buildConditionExpression(fieldName, operator, conditionValue);
    }
}
