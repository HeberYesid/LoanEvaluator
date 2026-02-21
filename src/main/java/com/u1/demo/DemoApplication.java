package com.u1.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.Scanner;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	@Profile("!test")
	public CommandLineRunner run() {
		return args -> {
			Scanner scanner = new Scanner(System.in);

			System.out.println("=== Evaluador de Prestamos ===");

			String name;
			while (true) {
				System.out.print("Nombre del solicitante: ");
				name = scanner.nextLine().trim();
				if (name.isEmpty()) {
					System.out.println("  Entrada invalida. El nombre no puede estar vacio.");
				} else if (!name.matches("[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ ]+")) {
					System.out.println("  Entrada invalida. El nombre solo puede contener letras y espacios.");
				} else {
					break;
				}
			}

			System.out.print("Edad: ");
			int age = Integer.parseInt(scanner.nextLine().trim());

			System.out.print("Puntaje de credito (0-850): ");
			int creditScore = Integer.parseInt(scanner.nextLine().trim());

			boolean hasDebts;
			while (true) {
				System.out.print("Tiene deudas actuales? (si/no): ");
				String debtsInput = scanner.nextLine().trim().toLowerCase();
				if (debtsInput.equals("si")) {
					hasDebts = true;
					break;
				} else if (debtsInput.equals("no")) {
					hasDebts = false;
					break;
				} else {
					System.out.println("  Entrada invalida. Por favor ingrese 'si' o 'no'.");
				}
			}

			System.out.print("Anos de empleo: ");
			int yearsEmployment = Integer.parseInt(scanner.nextLine().trim());

			System.out.print("Ingreso mensual: ");
			double monthlyIncome = Double.parseDouble(scanner.nextLine().trim());

			System.out.print("Monto del prestamo solicitado: ");
			double loanAmount = Double.parseDouble(scanner.nextLine().trim());

			try {
				Applicant applicant = new Applicant(name, age, creditScore, hasDebts, yearsEmployment, monthlyIncome);
				LoanDecision decision = LoanEvaluator.evaluateLoan(applicant, loanAmount);

				System.out.println("\n--- Resultado ---");
				System.out.println("Solicitante : " + name);
				System.out.println("Decision    : " + decision);
			} catch (IllegalArgumentException e) {
				System.out.println("\nError en los datos ingresados: " + e.getMessage());
			}

			scanner.close();
		};
	}

}
