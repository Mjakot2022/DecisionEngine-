package com.assignment;

/** Represents a loan request with personal code, requested amount, and loan period. */
public class LoanRequest {
    private String personalCode;
    private int loanAmount;
    private int loanPeriod;

    public void setPersonalCode(String personalCode) {
        this.personalCode = personalCode;
    }

    public void setLoanAmount(int loanAmount) {
        this.loanAmount = loanAmount;
    }

    public void setLoanPeriod(int loanPeriod) {
        this.loanPeriod = loanPeriod;
    }

    public String getPersonalCode() {
        return personalCode;
    }

    public int getLoanAmount() {
        return loanAmount;
    }

    public int getLoanPeriod() {
        return loanPeriod;
    }

}
