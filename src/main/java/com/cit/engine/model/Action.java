package com.cit.engine.model;

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
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fieldName;

    private String actionValue;

    @Column(length = 1000)
    private String expression;

    public void generateExpression() {
        this.expression = SpELExpressionBuilder.buildActionExpression(fieldName, actionValue);
    }
}
