package com.cit.engine.repository;

import com.cit.engine.enums.RuleType;
import com.cit.engine.model.BusinessRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusinessRuleRepository extends JpaRepository<BusinessRule, Long> {
    List<BusinessRule> findByRuleType(RuleType ruleType);
}

