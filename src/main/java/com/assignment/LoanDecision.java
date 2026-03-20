package com.assignment;


public class LoanDecision {
    private String decision;
    private int amount;

    public LoanDecision(String decision, int amount) {
        this.decision = decision;
        this.amount = amount;
    }

    public String getDecision() {
        return decision;
    }

    public int getAmount() {
        return amount;
    }
}
