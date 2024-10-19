package assignment1;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reveal implements Runnable {

	// Class constants
	private static final String MISSING = "Missing";
	private static final String INVALID = "Invalid";

	// Reveal section to be processed
	private final String section;

	// Reveal constructor
	public Reveal(String section) {
		this.section = section;
	}

	@Override
	public void run() {

		// Extract relevant data from the first section
		String lastName = extractElement("L", section);
		String firstName = extractElement("F", section);
		String middleInitial = extractElement("M", section);
		String birthDate = extractElement("B", section).replace(",", "");
		String age = calculateAge(birthDate);

		// Display results 30 times
		for (int i = 0; i < 1; i++) {
			System.out.printf("SECTION ONE IS: %s\n", section);
			System.out.printf("LAST NAME IS: %s\n", lastName.equals("Missing") ? "Missing" : lastName);
			System.out.printf("FIRST NAME IS: %s\n", firstName.equals("Missing") ? "Missing" : firstName);
			System.out.printf("MIDDLE INITIAL IS: %s\n", middleInitial.equals("Missing") ? "Missing" : middleInitial);
			System.out.printf("AGE IS: %s\n", age);
		}
	}

	/**
	 * Extracts first name, middle initial, last name, & DOB from section based on
	 * prefix and returns it or returns MISSING if not found
	 */
	private String extractElement(String prefix, String section) {
		section = section.replaceAll("\\s+", " "); // Removing unnecessary spaces

		String regex;

		// Determine regex pattern based on the prefix
		if ("L".equals(prefix)) {
			regex = "L(\\w+?)(\\s|F|M|B|$)"; // Matches last name
		} else if ("F".equals(prefix)) {
			regex = "(?:M\\w\\s*)?F\\s*(\\w+?)(\\s|L|M|B|$)";// Matches first name
		} else if ("M".equals(prefix)) {
			regex = "M(\\w)(\\s|F|L|B|$)"; // Matches middle initial
		} else if ("B".equals(prefix)) {
			regex = "B(\\d{2},?\\d{2},?\\d{4})";// Matches birth date
		} else {
			return MISSING;
		}

		// Match the regex pattern to the section and return the extracted string
		Matcher matcher = Pattern.compile(regex).matcher(section);
		if (matcher.find()) {
			return matcher.group(1).trim();
		}

		return MISSING;
	}

	/**
	 * Calculates age in years, months, and days. Assume no leap years exist and all
	 * months are 30 days in length.
	 */
	private String calculateAge(String birthDate) {
		if (birthDate.length() != 8) {
			return INVALID;
		}

		try {
			LocalDate currentDate = LocalDate.now();
			int month = Integer.parseInt(birthDate.substring(0, 2));
			int day = Integer.parseInt(birthDate.substring(2, 4));
			int year = Integer.parseInt(birthDate.substring(4, 8));

			// Validate month, day, and year
			if (month < 1 || month > 12) {
				return INVALID;
			}
			if (day < 1 || day > 31) { // assumed all months have 30 days
				return INVALID;
			}
			if (year > currentDate.getYear()) { // birth year should be in the past
				return INVALID;
			}

			// Calculate age in days
			int totalDaysBirth = year * 360 + month * 30 + day;
			int currentTotalDays = currentDate.getYear() * 360 + currentDate.getMonthValue() * 30
					+ currentDate.getDayOfMonth();
			int ageInDays = currentTotalDays - totalDaysBirth;

			// Convert age in days to years, months, and days
			int years = ageInDays / 360;
			int months = (ageInDays % 360) / 30;
			int days = (ageInDays % 360) % 30;

			return String.format("%d years, %d months, and %d days.", years, months, days);
		} catch (NumberFormatException e) {
			return INVALID;
		}

	}
}