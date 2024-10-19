package assignment1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Generator implements Runnable {

	// Generator section to be processed
	private final String section;

	// Generator constructor
	public Generator(String section) {
		this.section = section;
	}

	@Override
	public void run() {
		// Extract numbers from section three
		List<Integer> numbers = extractNumbers(section);

		// Generate a password from extracted numbers
		String password = generatePassword(numbers);

		// Display results 40 times
		for (int i = 0; i < 1; i++) {
			System.out.printf("SECTION THREE IS: %s\n", section);
			System.out.printf("NUMBER OF VALID SCORES: %d\n", numbers.size());
			System.out.printf("PASSWORD IS: %s\n", password);
		}
	}

	// Extract all valid non-negative numbers
	private List<Integer> extractNumbers(String section) {

		String[] parts = section.split(",");
		List<Integer> numbers = new ArrayList<>();

		for (String part : parts) {
			try {
				int number = Integer.parseInt(part.trim());
				if (number >= 0) {
					numbers.add(number);
				}
			} catch (NumberFormatException e) {
				System.err.printf("Invalid number format encountered: %s\n", part);
			}
		}
		return numbers;
	}

	// Generate password on given password creation criteria
	private String generatePassword(List<Integer> numbers) {

		// Define alphabet and symbols for password creation
		char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		char[] symbols = "!@#45%8967".toCharArray();

		// Find the max and min using stream API
		int max = numbers.stream().max(Integer::compareTo).orElse(0);
		int min = numbers.stream().min(Integer::compareTo).orElse(0);

		// Calculate new sets of numbers based on max and min
		Set<Integer> new1 = new HashSet<>();
		for (int number : numbers) {
			new1.add(max - number);
		}

		Set<Integer> new2 = new HashSet<>();
		for (int number : numbers) {
			new2.add(number - min);
		}

		// Modify the sets per given criteria
		new2.removeAll(new1);
		new1.addAll(new2);

		// Calculate modulo values for password characters
		List<Integer> alphabetIndices = new ArrayList<>();
		for (int number : new1) {
			alphabetIndices.add(number % 26);
		}

		// Map numbers to alphabet
		StringBuilder letters = new StringBuilder();
		for (int number : alphabetIndices) {
			letters.append(alphabet[number]);
		}

		// Map numbers to symbol
		List<Integer> sybolIndices = new ArrayList<>();
		for (int number : alphabetIndices) {
			sybolIndices.add(number % 10);
		}
		StringBuilder symbol = new StringBuilder();
		for (int number : sybolIndices) {
			symbol.append(symbols[number]);
		}

		// Combine letters and symbols to create password
		StringBuilder password = new StringBuilder();
		for (int i = 0; i < letters.length(); i++) {
			password.append(letters.charAt(i));
			if (i < symbol.length()) {
				password.append(symbol.charAt(i));
			}
		}
		return password.toString();
	}
}