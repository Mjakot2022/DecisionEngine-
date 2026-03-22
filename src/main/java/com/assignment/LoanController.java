package com.assignment;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoanController {

    private final DecisionEngine decisionEngine = new DecisionEngine();

    @PostMapping("/loan/decision")
    public LoanDecision getDecision(@RequestBody LoanRequest request){
        return decisionEngine.calculateDecision(request);

    }

}
