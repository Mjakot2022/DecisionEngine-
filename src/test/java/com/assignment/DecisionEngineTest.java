package com.assignment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Unit tests for DecisionEngine to verify loan decisions for different scenarios. */
class DecisionEngineTest {

    private final DecisionEngine engine = new DecisionEngine();

    @Test
    void testPersonWithDebt() {
        LoanRequest request = new LoanRequest();
        request.setPersonalCode("49002010965");
        request.setLoanAmount(5000);
        request.setLoanPeriod(24);

        LoanDecision decision = engine.calculateDecision(request);

        assertEquals("NEGATIVE", decision.getDecision());
        assertEquals(0, decision.getAmount());
    }

    @Test
    void testApproved() {
        LoanRequest request = new LoanRequest();
        request.setPersonalCode("49002010976"); // segment1
        request.setLoanAmount(3000);
        request.setLoanPeriod(24);

        LoanDecision decision = engine.calculateDecision(request);

        assertEquals("POSITIVE", decision.getDecision());
        assertTrue(decision.getAmount() >= 2000);
    }

    @Test
    void testMaxAmount() {
        LoanRequest request = new LoanRequest();
        request.setPersonalCode("49002010987"); // segment2
        request.setLoanAmount(10000);
        request.setLoanPeriod(36);

        LoanDecision decision = engine.calculateDecision(request);

        assertEquals("POSITIVE", decision.getDecision());
        assertTrue(decision.getAmount() <= 10000);
    }

    @Test
    void testUnknownCode() {
        LoanRequest request = new LoanRequest();
        request.setPersonalCode("12345678901"); // unknown
        request.setLoanAmount(5000);
        request.setLoanPeriod(24);

        LoanDecision decision = engine.calculateDecision(request);

        assertEquals("NEGATIVE", decision.getDecision());
        assertEquals(0, decision.getAmount());
    }
}