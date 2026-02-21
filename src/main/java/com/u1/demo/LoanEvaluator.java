package com.u1.demo;

public final class LoanEvaluator {

	private LoanEvaluator() {
	}

	/**
	 * Preconditions:
	 * - applicant must not be null.
	 * - applicant must satisfy its invariant (valid state).
	 * - amount must be greater than zero.
	 * - amount must not exceed 5 times the applicant monthly income.
	 *
	 * Postconditions:
	 * - returns one of: APPROVED, REJECTED, MANUAL_REVIEW.
	 * - the returned value is consistent with all business rules.
	 *
	 * Input/Output relation:
	 * - output depends on amount and applicant attributes:
	 *   creditScore, hasDebts, yearsEmployment, and monthlyIncome.
	 */
	public static LoanDecision evaluateLoan(Applicant applicant, double amount) {
		validatePreconditions(applicant, amount);

		LoanDecision decision;
		int score = applicant.getCreditScore();

		if (score >= 700) {
			decision = applicant.isHasDebts() ? LoanDecision.MANUAL_REVIEW : LoanDecision.APPROVED;
		} else if (score < 500) {
			decision = LoanDecision.REJECTED;
		} else {
			decision = applicant.getYearsEmployment() > 2 ? LoanDecision.MANUAL_REVIEW : LoanDecision.REJECTED;
		}

		validatePostconditions(applicant, amount, decision);
		return decision;
	}

	private static void validatePreconditions(Applicant applicant, double amount) {
		if (applicant == null) {
			throw new IllegalArgumentException("Applicant cannot be null.");
		}
		applicant.verifyInvariant();
		if (amount <= 0) {
			throw new IllegalArgumentException("Loan amount must be greater than zero.");
		}
		double maxAllowed = applicant.getMonthlyIncome() * 5;
		if (amount > maxAllowed) {
			throw new IllegalArgumentException("Loan amount cannot exceed 5 times monthly income.");
		}
	}

	private static void validatePostconditions(Applicant applicant, double amount, LoanDecision decision) {
		if (decision == null) {
			throw new IllegalStateException("Decision cannot be null.");
		}

		if (amount > applicant.getMonthlyIncome() * 5) {
			throw new IllegalStateException("Postcondition failed: invalid amount accepted.");
		}

		int score = applicant.getCreditScore();
		if (score >= 700 && !applicant.isHasDebts() && decision != LoanDecision.APPROVED) {
			throw new IllegalStateException("Postcondition failed: should be APPROVED.");
		}
		if (score >= 700 && applicant.isHasDebts() && decision != LoanDecision.MANUAL_REVIEW) {
			throw new IllegalStateException("Postcondition failed: should be MANUAL_REVIEW.");
		}
		if (score < 500 && decision != LoanDecision.REJECTED) {
			throw new IllegalStateException("Postcondition failed: should be REJECTED.");
		}
		if (score >= 500 && score <= 699) {
			LoanDecision expected = applicant.getYearsEmployment() > 2
				? LoanDecision.MANUAL_REVIEW
				: LoanDecision.REJECTED;
			if (decision != expected) {
				throw new IllegalStateException("Postcondition failed: inconsistent decision for mid credit score.");
			}
		}
	}
}