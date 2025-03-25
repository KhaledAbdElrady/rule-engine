package com.cit.engine.service;

import com.cit.engine.enums.RuleType;
import com.cit.engine.exception.RuleNotFoundException;
import com.cit.engine.model.Action;
import com.cit.engine.model.BusinessRule;
import com.cit.engine.model.Condition;
import com.cit.engine.repository.BusinessRuleRepository;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@CacheConfig(cacheNames = "businessRules")
public class BusinessRuleService {

    private final BusinessRuleRepository ruleRepository;


    public BusinessRuleService(BusinessRuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    @Cacheable("businessRules") // Cache all rules
    public List<BusinessRule> getAllRules() {
        return ruleRepository.findAll();
    }

    @Cacheable(value = "businessRules", key = "'ENRICHMENT'")// Cache only enrichment rules
    public List<BusinessRule> getEnrichmentRules() {
        return ruleRepository.findByRuleType(RuleType.ENRICHMENT);
    }

    @Cacheable(value = "businessRules", key = "'ROUTING'")// Cache only routing rules
    public List<BusinessRule> getRoutingRules() {
        return ruleRepository.findByRuleType(RuleType.ROUTING);
    }

    public BusinessRule getRuleById(Long id) {
        return ruleRepository.findById(id)
                .orElseThrow(() -> new RuleNotFoundException("Rule with ID " + id +  " does not exist."));
    }

    @Transactional
    @CacheEvict(allEntries = true) // Clear cache when a rule is created
    public BusinessRule createRule(BusinessRule rule) {
        rule.getConditions().forEach(Condition::generateExpression);
        rule.getActions().forEach(Action::generateExpression);
        return ruleRepository.save(rule);
    }

    @Transactional
    @CacheEvict(allEntries = true) // Clear cache when a rule is updated
    public BusinessRule updateRule(Long id, BusinessRule updatedRule) {
        return ruleRepository.findById(id)
                .map(existingRule -> {
                    existingRule.setName(updatedRule.getName());
                    existingRule.setRuleType(updatedRule.getRuleType());
                    existingRule.setPriority(updatedRule.getPriority());

                    existingRule.getConditions().clear();
                    updatedRule.getConditions().forEach(condition -> {
                        condition.generateExpression();
                        existingRule.getConditions().add(condition);
                    });

                    existingRule.getActions().clear();
                    updatedRule.getActions().forEach(action -> {
                        action.generateExpression();
                        existingRule.getActions().add(action);
                    });

                    return ruleRepository.save(existingRule);
                })
                .orElseThrow(() -> new RuleNotFoundException("Rule with ID " + id + " does not exist."));
    }

    @Transactional
    @CacheEvict(allEntries = true) // Clear cache when a rule is deleted
    public String deleteRule(Long id) {
        if (!ruleRepository.existsById(id)) {
            throw new RuleNotFoundException("Rule with ID " + id + " does not exist.");
        }
        ruleRepository.deleteById(id);
        return "Rule with ID " + id + " has been deleted successfully.";
    }
}
