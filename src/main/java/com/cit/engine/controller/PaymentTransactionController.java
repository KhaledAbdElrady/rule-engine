package com.cit.engine.controller;

import com.cit.engine.model.PaymentTransaction;
import com.cit.engine.model.RuleEngineResult;
import com.cit.engine.repository.PaymentTransactionRepository;
import com.cit.engine.service.RuleEngine;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class PaymentTransactionController {

    private final PaymentTransactionRepository transactionRepository;
    private final RuleEngine ruleEngine;

    public PaymentTransactionController(PaymentTransactionRepository transactionRepository, RuleEngine ruleEngine) {
        this.transactionRepository = transactionRepository;
        this.ruleEngine = ruleEngine;
    }

    @PostMapping("/execute")
    public ResponseEntity<RuleEngineResult> executeRules(@RequestBody PaymentTransaction transaction) {
        RuleEngineResult ruleEngineResult = ruleEngine.process(transaction);
        transactionRepository.save(ruleEngineResult.transaction());
        return ResponseEntity.ok(ruleEngineResult);
    }
}
