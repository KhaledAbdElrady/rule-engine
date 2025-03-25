package com.cit.engine.controller;

import com.cit.engine.model.BusinessRule;
import com.cit.engine.service.BusinessRuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rules")
public class BusinessRuleController {

    private final BusinessRuleService ruleService;

    public BusinessRuleController(BusinessRuleService ruleService) {
        this.ruleService = ruleService;
    }

    @GetMapping
    public ResponseEntity<List<BusinessRule>> getAllRules() {
        return ResponseEntity.ok(ruleService.getAllRules());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusinessRule> getRuleById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ruleService.getRuleById(id));
    }

    @PostMapping
    public ResponseEntity<BusinessRule> createRule(@RequestBody BusinessRule rule) {
        return ResponseEntity.ok(ruleService.createRule(rule));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BusinessRule> updateRule(@PathVariable("id") Long id,@RequestBody BusinessRule rule) {
        return ResponseEntity.ok(ruleService.updateRule(id, rule));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRule(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ruleService.deleteRule(id));
    }
}
