package com.assignment;

/**
 * The DecisionEngine class evaluates loan requests and provides a decision
 * indicating whether the loan is approved or rejected.
 * <p>
 * It uses the client's personal code and requested loan period to determine
 * the maximum approvable loan amount and returns the result as a LoanDecision object.
 */

public class DecisionEngine {

    public enum DecisionStatus {
        POSITIVE, NEGATIVE
    }

    private static final int MIN_PERIOD = 12;
    private static final int MAX_PERIOD = 60;
    private static final int MIN_APPROVABLE_AMOUNT = 2000;
    private static final int MAX_APPROVABLE_AMOUNT = 10000;

    public LoanDecision calculateDecision(LoanRequest request) {

        int period = request.getLoanPeriod();
        int creditModifier = getCreditModifier(request.getPersonalCode());

        if (creditModifier <= 0 || period < MIN_PERIOD || period > MAX_PERIOD) {
            return new LoanDecision(DecisionStatus.NEGATIVE.name(), 0);
        }

        int maxAmount = Math.min(creditModifier * period, MAX_APPROVABLE_AMOUNT);

        if (maxAmount >= MIN_APPROVABLE_AMOUNT) {
            return new LoanDecision(DecisionStatus.POSITIVE.name(), maxAmount);
        }

        for (int newPeriod = Math.max(period + 1, MIN_PERIOD); newPeriod <= MAX_PERIOD; newPeriod++) {
            maxAmount = Math.min(creditModifier * newPeriod, MAX_APPROVABLE_AMOUNT);
            if (maxAmount >= MIN_APPROVABLE_AMOUNT) {
                return new LoanDecision(DecisionStatus.POSITIVE.name(), maxAmount);
            }
        }

        return new LoanDecision(DecisionStatus.NEGATIVE.name(), 0);
    }


    private int getCreditModifier(String personalCode) {
        return switch (personalCode) {
            case "49002010965" -> 0;
            case "49002010976" -> 100;
            case "49002010987" -> 300;
            case "49002010998" -> 1000;
            default -> -1;
        };
    }
}
