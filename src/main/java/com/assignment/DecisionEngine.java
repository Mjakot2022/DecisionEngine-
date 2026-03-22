package com.assignment;

public class DecisionEngine {
    public LoanDecision calculateDecision(LoanRequest request) {
        int creditModifier = getCreditModifier(request.getPersonalCode());
        if (creditModifier == 0) {
            return new LoanDecision("NEGATIVE", 0);
        }
        if (request.getLoanPeriod() < 12 || request.getLoanPeriod() > 60) {
            return new LoanDecision("NEGATIVE", 0);
        }
        int period = request.getLoanPeriod();
        int maxAmount = Math.min(creditModifier * period, 10000);

        if (maxAmount >= 2000) {
            return new LoanDecision("POSITIVE", maxAmount);
        }
        for (int newPeriod = Math.max(period + 1, 12); newPeriod <= 60; newPeriod++) {
            maxAmount = Math.min(creditModifier * newPeriod, 10000);
            if (maxAmount >= 2000) {
                return new LoanDecision("POSITIVE", maxAmount);
            }
        }

        return new LoanDecision("NEGATIVE", 0);
    }




    private int getCreditModifier(String personalCode) {
        return switch (personalCode) {
            case "49002010965" -> 0;
            case "49002010976" -> 100;
            case "49002010987" -> 300;
            case "49002010998" -> 1000;
            default -> throw new IllegalArgumentException("Unknown code: " + personalCode);
        };
    }
}
