package com.demo.crypto.enigma;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.demo.crypto.enigma.model.EnigmaMachine;
import com.demo.crypto.enigma.model.SteckerCable;
import com.demo.crypto.enigma.util.ConfigurationUtil;
import com.demo.crypto.enigma.util.SteckerCombinationTracker;

public class EnigmaBreakerControl {

	public static void main( final String[] arguments ) throws IOException {
		System.out.println("First enter a plain text message, or select a number to choose one of these WWII samples:");
		System.out.println("  1. ANXGENERALHELLOWORLD  (translation: To General...)");
		System.out.println("  2. KEINEBESONDERENEREIGNISSE (translation: No sepecial occurrences)");
		System.out.print("  => ");
		
		BufferedReader reader = new BufferedReader( new InputStreamReader(System.in) );
		String input = reader.readLine();
		
		String startPlainText = null;
		if( "1".equals(input) )
			startPlainText = "ANXGENERALHELLOWORLD"; // deciphers to ANXGENERALHELLOWORLD
		else if( "2".equals(input) )
			startPlainText = "KEINEBESONDERENEREIGNISSE";
		else
			startPlainText = input;
		
		char[] initialPositions = getInitialPositions(reader);
		
		List<SteckerCable> steckeredPairs = getSteckeredPairs(reader);
		
		System.out.println("\nEncrypting the chosen plain text with rotor positions " + Arrays.toString(initialPositions) + " and steckered pairs: " + steckeredPairs);
		for( int timer=0; timer<startPlainText.length(); timer++) {
			try {
				System.out.print("* ");
				Thread.sleep(25);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		EnigmaMachine enigmaMachine = new EnigmaMachine(initialPositions, steckeredPairs);
		String cipherText = enigmaMachine.encrypt(startPlainText);
		System.out.println("\n\nWe encrypted your plain text " + startPlainText + " to " + cipherText + ". Now, let's see if we can decrypt it back now by re-discovering the rotor and stecker settings (these settings are Enigma's encryption 'key').");
		
		int threadCount = getThreadCount(reader);
		
		String endPlainText = null;
		
		List<EnigmaBreaker> enigmaBreakers = new ArrayList<EnigmaBreaker>();
		for( int threadIndex=0; threadIndex<threadCount; threadIndex++ ) {
			EnigmaBreaker enigmaBreaker = new EnigmaBreaker(cipherText, steckeredPairs.size(), threadIndex, threadCount);
			enigmaBreakers.add( enigmaBreaker );
			enigmaBreaker.start();
		}
		
		char[] solvedPositions = new char[3];
		List<SteckerCable> solvedSteckeredPairs;
		
		while(true) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			EnigmaBreaker stoppedEnigmaBreaker = null;
			boolean stillRunning = false;
			for( EnigmaBreaker enigmaBreaker : enigmaBreakers ) {
				if( enigmaBreaker.getSolvedPositions()!=null ) {
					stoppedEnigmaBreaker = enigmaBreaker;
					break;
				}
			}
			
			if( stoppedEnigmaBreaker==null ) {
				for( EnigmaBreaker enigmaBreaker : enigmaBreakers ) {
					if( enigmaBreaker.isAlive() ) {
						stillRunning = true;
						break;
					}
				}
			}
			
			if( stoppedEnigmaBreaker!=null ) {
				System.out.println(stoppedEnigmaBreaker + " has returned with results! interrupting other threads now.");
				
				for( EnigmaBreaker enigmaBreaker : enigmaBreakers ) // first interrupt any remaining live threads
					enigmaBreaker.interrupt();
				
				// then pull the solved positions info from the thread that returned successfully
				solvedPositions = stoppedEnigmaBreaker.getSolvedPositions();
				solvedSteckeredPairs = stoppedEnigmaBreaker.getSolvedSteckeredPairs();
				break;
			}
			else if(!stillRunning) {
				System.out.println("no solution found!");
			}
		}
		
		enigmaMachine.setRotors(solvedPositions);
		enigmaMachine.setSteckers(solvedSteckeredPairs);
		
		endPlainText = enigmaMachine.decrypt(cipherText);
		System.out.println("End plain text: " + endPlainText);
	}


	private static char[] getInitialPositions( final BufferedReader reader ) throws IOException {
		char[] initialPositions = null;
		
		while(true) {
			System.out.println("Now, enter a sequence of starting rotor positions, from left rotor to right. for example: ARH. Or, press enter for random positions: ");
			System.out.print("  => ");
			String input = reader.readLine();
			
			if( StringUtils.isBlank(input) ) {
				initialPositions = ConfigurationUtil.generateRandomRotorPositions();
				System.out.println("Randomly chosen rotor positions to encrypt with: " + Arrays.toString(initialPositions));
				break;
			}
			else {
				try {
					initialPositions = ConfigurationUtil.getPositionsFromString(input);
					System.out.println("Using your chosen rotor positions to encrypt with: " + Arrays.toString(initialPositions));
					break;
				}
				catch(IllegalArgumentException illegalArgumentException) {
					System.out.println( illegalArgumentException.getMessage() );
				}
			}
		}
	
		return initialPositions;
	}
	
	
	private static List<SteckerCable> getSteckeredPairs( final BufferedReader reader ) throws IOException {
		List<SteckerCable> steckeredPairs = null;
		
		while(true) {
			System.out.println("Finally, enter a number of stecker cables to use, 0 through 10. Or press enter to use the default (" + SteckerCombinationTracker.DEFAULT_STECKER_PAIR_COUNT + ").");
			System.out.println(" *tip: more than 3-4 steckers significantly increases processing time for decryption.");
			System.out.print("  => ");
			String input = reader.readLine();
			if( StringUtils.isBlank(input) ) {
				System.out.println("Using default of " + SteckerCombinationTracker.DEFAULT_STECKER_PAIR_COUNT + " stecker cables.");
				break;
			}
			else {
				try {
					steckeredPairs = ConfigurationUtil.generateRandomSteckers(input);
					System.out.println("Using your chosen stecker count: " + input);
					break;
				}
				catch(IllegalArgumentException illegalArgumentException) {
					System.out.println( illegalArgumentException.getMessage() );
				}
			}
		}
		
		return steckeredPairs;
	}
	
	
	private static int getThreadCount( final BufferedReader reader ) throws IOException {
		int threadCount = 1;
		
		while(true) {
			System.out.println("Enter the number of parallel threads you'd like to use for for breaking the cipher; 1, 2, 4, or 8. Or press enter to use the default (1). ");
			System.out.println(" *tip: use one thread per CPU core for fastest cipher break.");
			System.out.print("  => ");
			String input = reader.readLine();
			if( StringUtils.isBlank(input) ) {
				System.out.println("Using default of 1 thread.");
				break;
			}
			else {
				try {
					threadCount = ConfigurationUtil.validateThreadCount(input);
					System.out.println("Using your chosen thread count: " + threadCount);
					break;
				}
				catch(IllegalArgumentException illegalArgumentException) {
					System.out.println( illegalArgumentException.getMessage() );
				}
			}
		}
		
		return threadCount;
	}
}
