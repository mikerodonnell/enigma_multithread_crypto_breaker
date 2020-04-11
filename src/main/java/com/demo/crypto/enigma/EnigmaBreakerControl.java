package com.demo.crypto.enigma;

import com.demo.crypto.enigma.model.EnigmaMachine;
import com.demo.crypto.enigma.model.SteckerCable;
import com.demo.crypto.enigma.util.ConfigurationUtil;
import com.demo.crypto.enigma.util.SteckerCombinationTracker;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * captures configuration options from the user, kicks of code breaking, monitors progress, and reports back to user upon completion.
 *
 * @author Mike O'Donnell  github.com/mikerodonnell
 */
public class EnigmaBreakerControl {

	public static void main(final String[] arguments) throws IOException, InterruptedException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String startPlainText = getStartPlainText(reader);

		char[] initialPositions = getInitialPositions(reader);

		List<SteckerCable> steckeredPairs = getSteckeredPairs(reader);

		System.out.println("\nEncrypting the chosen plain text with rotor positions " + Arrays.toString(initialPositions) + " and steckered pairs: " + steckeredPairs);
		for (int timer = 0; timer < startPlainText.length(); timer++) {
			try {
				System.out.print("* ");
				Thread.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		EnigmaMachine enigmaMachine = new EnigmaMachine(initialPositions, steckeredPairs);
		String cipherText = enigmaMachine.encrypt(startPlainText);
		System.out.println("\n\nWe encrypted your plain text to " + cipherText + ". Now, let's see if we can decrypt it back by re-discovering the rotor and stecker settings (these settings are Enigma's encryption 'key').");

		int threadCount = getThreadCount(reader);
		System.out.println("\nHere we go ...\n");

		String endPlainText = null;

		List<EnigmaBreaker> enigmaBreakers = new ArrayList<EnigmaBreaker>();
		for (int threadIndex = 0; threadIndex < threadCount; threadIndex++) {
			EnigmaBreaker enigmaBreaker = new EnigmaBreaker(cipherText, steckeredPairs.size(), threadIndex, threadCount);
			enigmaBreakers.add(enigmaBreaker);
			enigmaBreaker.start();
		}

		char[] solvedPositions = null;
		List<SteckerCable> solvedSteckeredPairs = null;

		while (true) {
			Thread.sleep(4000);

			EnigmaBreaker stoppedEnigmaBreaker = null;
			boolean stillRunning = false;
			for (EnigmaBreaker enigmaBreaker : enigmaBreakers) {
				if (enigmaBreaker.getSolvedPositions() != null) {
					stoppedEnigmaBreaker = enigmaBreaker;
					break;
				}
			}

			if (stoppedEnigmaBreaker == null) {
				for (EnigmaBreaker enigmaBreaker : enigmaBreakers) {
					if (enigmaBreaker.isAlive()) {
						stillRunning = true;
						break;
					}
				}
			}

			if (stoppedEnigmaBreaker != null) {
				System.out.println(stoppedEnigmaBreaker + " has returned with results! interrupting other threads now.\n");

				for (EnigmaBreaker enigmaBreaker : enigmaBreakers) // first interrupt any remaining live threads
					enigmaBreaker.interrupt();

				// then pull the solved positions info from the thread that returned successfully
				solvedPositions = stoppedEnigmaBreaker.getSolvedPositions();
				solvedSteckeredPairs = stoppedEnigmaBreaker.getSolvedSteckeredPairs();
				break;
			} else if (!stillRunning) {
				break;
			}
		}

		if (solvedPositions == null) {
			System.out.println("\nno solution found! perhaps this sample message matches multiple cribs, and the breaker chose the wrong one?\n\n");
		} else {
			enigmaMachine.setRotors(solvedPositions);
			enigmaMachine.setSteckers(solvedSteckeredPairs);
			endPlainText = enigmaMachine.decrypt(cipherText);
			System.out.println("End plain text: " + endPlainText + "\n\n");
		}
	}

	/**
	 * Capture the plain text (in German) sample to use for the simulation, either from user input for a set of preset options.
	 *
	 * @param reader
	 * @return a plain text String of the text to be encoded.
	 * @throws IOException
	 */
	private static String getStartPlainText(final BufferedReader reader) throws IOException {
		String startPlainText = null;

		final File sampleMessageDirectory = new File("src/main/resources/sample/message");
		final File[] sampleMessageFiles = sampleMessageDirectory.listFiles();
		final List<Properties> sampleMessages = new ArrayList<Properties>();

		System.out.println("First enter a plain text message, or select a number to choose one of these WWII samples:");
		for (int index = 0; index < sampleMessageFiles.length; index++) {
			FileInputStream sampleMessage = new FileInputStream(sampleMessageFiles[index]);
			Properties properties = new Properties();
			properties.load(sampleMessage);
			sampleMessages.add(properties);

			System.out.println("  " + (index + 1) + ". " + properties.getProperty("plain_text"));
			System.out.println("    (translation: " + properties.getProperty("translation"));
		}

		while (true) {
			System.out.print("  => ");

			String input = reader.readLine().trim();

			if (StringUtils.isNotBlank(input)) {
				int sampleMessageChoice = -1;

				if (input.length() == 1) {
					try {
						sampleMessageChoice = Integer.valueOf(input);
						Validate.isTrue(sampleMessageChoice > 0);
						Validate.isTrue(sampleMessageChoice <= sampleMessages.size());
						startPlainText = sampleMessages.get(sampleMessageChoice - 1).getProperty("plain_text");
					} catch (IllegalArgumentException exception) {
					}
				} else {
					startPlainText = input;
				}
			}

			if (startPlainText == null) {
				System.out.println("Please enter a plain text message, or select a number to choose one of the above samples:");
			} else {
				break;
			}
		}

		return startPlainText;
	}

	private static char[] getInitialPositions(final BufferedReader reader) throws IOException {
		char[] initialPositions = null;

		while (true) {
			System.out.println("Now, enter a sequence of starting rotor positions, from left rotor to right. for example: ARH. Or, press enter for random positions: ");
			System.out.print("  => ");
			String input = reader.readLine().trim();

			if (StringUtils.isBlank(input)) {
				initialPositions = ConfigurationUtil.generateRandomRotorPositions();
				System.out.println("Randomly chosen rotor positions to encrypt with: " + Arrays.toString(initialPositions));
				break;
			} else {
				try {
					initialPositions = ConfigurationUtil.getPositionsFromString(input);
					System.out.println("Using your chosen rotor positions to encrypt with: " + Arrays.toString(initialPositions));
					break;
				} catch (IllegalArgumentException illegalArgumentException) {
					System.out.println(illegalArgumentException.getMessage());
				}
			}
		}

		return initialPositions;
	}

	private static List<SteckerCable> getSteckeredPairs(final BufferedReader reader) throws IOException {
		List<SteckerCable> steckeredPairs = new ArrayList<SteckerCable>();

		while (true) {
			System.out.println("Finally, enter a number of stecker cables to use, 0 through 10. Or press enter to use the default (" + SteckerCombinationTracker.DEFAULT_STECKER_PAIR_COUNT + ").");
			System.out.println(" *tip: 0-2 pairs cracks in minutes, 3-5 pairs cracks in hours, 6-10 pairs cracks in days!");
			System.out.print("  => ");
			String input = reader.readLine().trim();
			if (StringUtils.isBlank(input)) {
				System.out.println("Using default of " + SteckerCombinationTracker.DEFAULT_STECKER_PAIR_COUNT + " stecker cables.");
				steckeredPairs = ConfigurationUtil.generateRandomSteckers(SteckerCombinationTracker.DEFAULT_STECKER_PAIR_COUNT);
				break;
			} else {
				try {
					steckeredPairs = ConfigurationUtil.generateRandomSteckers(input);
					System.out.println("Using your chosen stecker count: " + input);
					break;
				} catch (IllegalArgumentException illegalArgumentException) {
					System.out.println(illegalArgumentException.getMessage());
				}
			}
		}

		return steckeredPairs;
	}

	private static int getThreadCount(final BufferedReader reader) throws IOException {
		int threadCount = 1;

		while (true) {
			System.out.println("Enter the number of parallel threads you'd like to use for for breaking the cipher; 1, 2, 4, or 8. Or press enter to use the default (1). ");
			System.out.println(" *tip: use one thread per CPU core for fastest cipher break.");
			System.out.print("  => ");
			String input = reader.readLine().trim();
			if (StringUtils.isBlank(input)) {
				System.out.println("Using default of 1 thread.");
				break;
			} else {
				try {
					threadCount = ConfigurationUtil.validateThreadCount(input);
					System.out.println("Using your chosen thread count: " + threadCount);
					break;
				} catch (IllegalArgumentException illegalArgumentException) {
					System.out.println(illegalArgumentException.getMessage());
				}
			}
		}

		return threadCount;
	}
}
