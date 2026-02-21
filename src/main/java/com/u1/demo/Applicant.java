package com.u1.demo;

public class Applicant {

	private String name;
	private int age;
	private int creditScore;
	private boolean hasDebts;
	private int yearsEmployment;
	private double monthlyIncome;

	public Applicant(String name, int age, int creditScore, boolean hasDebts, int yearsEmployment, double monthlyIncome) {
		this.name = name;
		this.age = age;
		this.creditScore = creditScore;
		this.hasDebts = hasDebts;
		this.yearsEmployment = yearsEmployment;
		this.monthlyIncome = monthlyIncome;
		verifyInvariant();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		verifyInvariant();
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
		verifyInvariant();
	}

	public int getCreditScore() {
		return creditScore;
	}

	public void setCreditScore(int creditScore) {
		this.creditScore = creditScore;
		verifyInvariant();
	}

	public boolean isHasDebts() {
		return hasDebts;
	}

	public void setHasDebts(boolean hasDebts) {
		this.hasDebts = hasDebts;
		verifyInvariant();
	}

	public int getYearsEmployment() {
		return yearsEmployment;
	}

	public void setYearsEmployment(int yearsEmployment) {
		this.yearsEmployment = yearsEmployment;
		verifyInvariant();
	}

	public double getMonthlyIncome() {
		return monthlyIncome;
	}

	public void setMonthlyIncome(double monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
		verifyInvariant();
	}

	public void verifyInvariant() {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Name cannot be null or blank.");
		}
		if (age < 18) {
			throw new IllegalArgumentException("Applicants under 18 years old cannot apply.");
		}
		if (creditScore < 0 || creditScore > 850) {
			throw new IllegalArgumentException("Credit score must be between 0 and 850.");
		}
		if (yearsEmployment < 0) {
			throw new IllegalArgumentException("Years of employment cannot be negative.");
		}
		if (monthlyIncome <= 0) {
			throw new IllegalArgumentException("Monthly income must be greater than zero.");
		}
	}
}