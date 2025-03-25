package com.cit.engine.model;

import java.util.List;

public record RuleEngineResult(PaymentTransaction transaction, int appliedRuleCount, List<String> appliedRuleNames) {
}
