package assignment1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * File: Assignment1
 * Class: CSCI 3341
 * Author: Junior Meador
 * Created on: October 9, 2023
 * Last Modified: October 23, 2023
 * Description: Multi-Threading using Java
 */


public class Main {

	public static void main(String[] args) {
		// Path to input file
		String filePath = "src/assignment1/xyz.txt";

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String record;

			while ((record = br.readLine()) != null) {
				// Display current record
				System.out.printf("%s\n", record);
				// Extract sections from the record
				String[] sections = extractSections(record);

				if (sections == null) {
					// If any section is missing, display invalid record message
					System.out.printf("Record is invalid\n");
				} else {

					// Create and initialize threads
					Thread revealThread = new Thread(new Reveal(sections[0]));
					Thread exposeThread = new Thread(new Expose(sections[1]));
					Thread generatorThread = new Thread(new Generator(sections[2]));

					// Start and wait for Reveal thread
					revealThread.start();
//					revealThread.join();

					// Start and wait for Expose thread
					exposeThread.start();
//					exposeThread.join();

					// Start and wait for Generator thread
					generatorThread.start();
//					generatorThread.join();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Extract the three sections from a given record
	private static String[] extractSections(String record) {
		// Remove all white spaces
		record = record.replaceAll("\\s+", "");

		// Define the regex pattern to identify different sections
		Pattern pattern = Pattern.compile("(\\*{1,3})([^*]+?)\\$");
		Matcher matcher = pattern.matcher(record);
		String[] sections = new String[3];

		// Assign each matched section to the appropriate position in array
		while (matcher.find()) {
			int asterisksCount = matcher.group(1).length();
			sections[asterisksCount - 1] = matcher.group(2).trim();
		}

		// Check for any missing sections
		for (String section : sections) {
			if (section == null) {
				return null;
			}
		}

		// Return the extracted sections
		return sections;
	}
}
