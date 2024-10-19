package assignment1;

import java.util.ArrayList;
import java.util.List;

public class Expose implements Runnable {

	// Expose section to be processed
	private final String section;

	// Expose constructor
	public Expose(String section) {
		this.section = section;
	}

	@Override
	public void run() {

		// Extract scores and perform statistical analysis from section two
		List<Integer> scores = extractScores(section);
		double average = calculateAverage(scores);
		double standardDeviation = calculateStandardDeviation(scores, average);
		List<Integer> outliers = identifyOutliers(scores, average, standardDeviation);

		// Display results 30 times
		for (int i = 0; i < 1; i++) {
			System.out.printf("SECTION TWO IS: %s\n", section);
			System.out.printf("NUMBER OF VALID SCORES: %d\n", scores.size());
			System.out.printf("THE AVERAGE IS: %.2f\n", average);
			System.out.printf("S.D. is: %.2f\n", standardDeviation);
			System.out.printf("OUTLIERS ARE: %s\n", outliers.isEmpty() ? "none"
					: String.join(", ", outliers.stream().map(String::valueOf).toArray(String[]::new)));
		}
	}

	/**
	 * Extracts valid scores from a comma-separated section string. Invalid scores
	 * and non-numeric values are ignored.
	 */
	private List<Integer> extractScores(String section) {
		String[] parts = section.split(",");
		List<Integer> scores = new ArrayList<>();
		for (String part : parts) {
			try {
				int score = Integer.parseInt(part.trim());
				if (score >= -5 && score <= 100) {
					scores.add(score);
				}
			} catch (NumberFormatException e) {
				System.err.printf("Invalid number format encountered: %s\n", part);
			}
		}
		return scores;
	}

	private double calculateAverage(List<Integer> scores) {
		double sum = 0;
		for (int score : scores) {
			sum += score;
		}
		return sum / scores.size();
	}

	private double calculateStandardDeviation(List<Integer> scores, double average) {
		double sum = 0;
		for (int score : scores) {
			sum += Math.pow(score - average, 2);
		}
		return Math.sqrt(sum / (scores.size() - 1));
	}

	private List<Integer> identifyOutliers(List<Integer> scores, double average, double standardDeviation) {
		List<Integer> outliers = new ArrayList<>();
		for (int score : scores) {
			if (Math.abs(score - average) > (2 * standardDeviation)) {
				outliers.add(score);
			}
		}
		return outliers;
	}
}