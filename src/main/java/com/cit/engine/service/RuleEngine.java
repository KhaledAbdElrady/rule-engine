package com.cit.engine.service;

import com.cit.engine.enums.RuleType;
import com.cit.engine.model.BusinessRule;
import com.cit.engine.model.PaymentTransaction;
import com.cit.engine.model.RuleEngineResult;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class RuleEngine {

    private final BusinessRuleService ruleService;
    private final ExpressionParser parser = new SpelExpressionParser();


    public RuleEngine(BusinessRuleService ruleService) {
        this.ruleService = ruleService;
    }

    public RuleEngineResult process(PaymentTransaction transaction) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("transaction", transaction);


        // Track applied rule names
        List<String> appliedRules = new ArrayList<>();

        // Apply Enrichment Rules (All Matching)
        ruleService.getEnrichmentRules().stream()
                .filter(rule -> rule.getRuleType() == RuleType.ENRICHMENT)
                .filter(rule -> evaluateCondition(rule, context))
                .sorted(Comparator.comparingInt(BusinessRule::getPriority))
                .forEach(rule -> {
                    applyRule(rule, transaction, context);
                    appliedRules.add(rule.getName());
                });

        // Apply the Highest Priority Matching Routing Rule
        ruleService.getRoutingRules().stream()
                .filter(rule -> rule.getRuleType() == RuleType.ROUTING)
                .filter(rule -> evaluateCondition(rule, context))
                .min(Comparator.comparingInt(BusinessRule::getPriority))
                .ifPresent(rule -> {
                    applyRule(rule, transaction, context);
                    appliedRules.add(rule.getName());
                });

        // Return transaction along with applied rule insights
        return new RuleEngineResult(transaction, appliedRules.size(), appliedRules);
    }

    private boolean evaluateCondition(BusinessRule rule, StandardEvaluationContext context) {
        return rule.getConditions().parallelStream()
                .allMatch(condition -> Boolean.TRUE.equals(
                        parser.parseExpression(condition.getExpression()).getValue(context, Boolean.class)
                ));
    }

    private void applyRule(BusinessRule rule, PaymentTransaction transaction, StandardEvaluationContext context) {
        rule.getActions().parallelStream().forEach(action -> {
            parser.parseExpression(action.getExpression()).getValue(context);
        });
        context.setVariable("transaction", transaction);
    }

}
