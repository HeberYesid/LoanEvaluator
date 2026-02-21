package com.u1.demo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoanEvaluatorTest {

	@Test
	void allowsAmountExactlyEqualToFiveTimesIncome() {
		Applicant applicant = new Applicant("Laura", 31, 720, false, 5, 1000);

		LoanDecision decision = LoanEvaluator.evaluateLoan(applicant, 5000);

		assertEquals(LoanDecision.APPROVED, decision);
	}

	@Test
	void approvesWhenHighScoreAndNoDebts() {
		Applicant applicant = new Applicant("Ana", 30, 750, false, 3, 2000);

		LoanDecision decision = LoanEvaluator.evaluateLoan(applicant, 5000);

		assertEquals(LoanDecision.APPROVED, decision);
	}

	@Test
	void manualReviewWhenHighScoreWithDebts() {
		Applicant applicant = new Applicant("Luis", 40, 710, true, 8, 3000);

		LoanDecision decision = LoanEvaluator.evaluateLoan(applicant, 6000);

		assertEquals(LoanDecision.MANUAL_REVIEW, decision);
	}

	@Test
	void rejectsWhenScoreBelow500() {
		Applicant applicant = new Applicant("Marta", 25, 450, false, 5, 1800);

		LoanDecision decision = LoanEvaluator.evaluateLoan(applicant, 2000);

		assertEquals(LoanDecision.REJECTED, decision);
	}

	@Test
	void manualReviewWhenMidScoreAndStableEmployment() {
		Applicant applicant = new Applicant("Carlos", 29, 650, false, 3, 2500);

		LoanDecision decision = LoanEvaluator.evaluateLoan(applicant, 4000);

		assertEquals(LoanDecision.MANUAL_REVIEW, decision);
	}

	@Test
	void rejectsWhenMidScoreAndNoStableEmployment() {
		Applicant applicant = new Applicant("Elena", 29, 650, false, 2, 2500);

		LoanDecision decision = LoanEvaluator.evaluateLoan(applicant, 4000);

		assertEquals(LoanDecision.REJECTED, decision);
	}

	@Test
	void failsPreconditionWhenAmountExceedsFiveTimesIncome() {
		Applicant applicant = new Applicant("Pablo", 33, 720, false, 4, 1000);

		assertThrows(IllegalArgumentException.class, () -> LoanEvaluator.evaluateLoan(applicant, 5000.01));
	}

	@Test
	void failsPreconditionWhenApplicantIsNull() {
		assertThrows(IllegalArgumentException.class, () -> LoanEvaluator.evaluateLoan(null, 1000));
	}

	@Test
	void failsPreconditionWhenAmountIsZero() {
		Applicant applicant = new Applicant("Diana", 28, 710, false, 3, 2000);

		assertThrows(IllegalArgumentException.class, () -> LoanEvaluator.evaluateLoan(applicant, 0));
	}

	@Test
	void failsPreconditionWhenAmountIsNegative() {
		Applicant applicant = new Applicant("Mario", 35, 710, false, 7, 2000);

		assertThrows(IllegalArgumentException.class, () -> LoanEvaluator.evaluateLoan(applicant, -1));
	}

	@Test
	void failsInvariantWhenApplicantIsUnderage() {
		assertThrows(IllegalArgumentException.class, () -> new Applicant("Sofia", 17, 700, false, 1, 1500));
	}
}