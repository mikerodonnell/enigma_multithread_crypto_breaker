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
		
		BufferedReader reader = new BufferedReader( new InputStreamReader(System.in) );
		String input = reader.readLine();
		
		String startPlainText = null;
		if( "1".equals(input) )
			startPlainText = "ANXGENERALHELLOWORLD"; // deciphers to ANXGENERALHELLOWORLD
		else if( "2".equals(input) )
			startPlainText = "KEINEBESONDERENEREIGNISSE";
		else
			startPlainText = input;
		
		input = null;
		System.out.print("Now, enter a sequence of starting rotor positions, from left rotor to right. for example: ABC. Or, press enter for random positions: ");
		input = reader.readLine();
		
		char[] initialPositions = null;
		if( StringUtils.isBlank(input) ) {
			initialPositions = ConfigurationUtil.generateRandomRotorPositions();
			System.out.println("Randomly chosen rotor positions to encrypt with: " + Arrays.toString(initialPositions));
		}
		else {
			initialPositions = ConfigurationUtil.getPositionsFromString(input);
			System.out.println("Using your chosen rotor positions to encrypt with: " + Arrays.toString(initialPositions));
		}
		
		input = null;
		System.out.println("Finally, enter a number of stecker cables to use, 0 through 10. Or press enter to use the default (" + SteckerCombinationTracker.DEFAULT_STECKER_PAIR_COUNT + ").");
		System.out.println("(more than 3-4 steckers significantly increases processing time for decryption)");
		input = reader.readLine();
		int steckerPairCount = SteckerCombinationTracker.DEFAULT_STECKER_PAIR_COUNT;
		if( StringUtils.isBlank(input) ) {
			System.out.println("Using default of " + SteckerCombinationTracker.DEFAULT_STECKER_PAIR_COUNT + " stecker cables.");
		}
		else {
			steckerPairCount = Integer.valueOf(input);
			System.out.println("Using your chosen stecker count: " + steckerPairCount);
		}
		
		List<SteckerCable> steckeredPairs = ConfigurationUtil.generateRandomSteckers(steckerPairCount);
		System.out.println("Encrypting the chosen plain text with rotor positions " + Arrays.toString(initialPositions) + " and steckered pairs: " + steckeredPairs);
		
		EnigmaMachine enigmaMachine = new EnigmaMachine(initialPositions, steckeredPairs);
		String cipherText = enigmaMachine.encrypt(startPlainText);
		System.out.println("We encrypted your plain text " + startPlainText + " to " + cipherText + ", let's see if we can decrypt it back now by re-discovering the rotor and stecker settings.");
		
		input = null;
		System.out.print("Enter the number of parallel threads you'd like to use for for breaking the cipher, 1 through 4. Or press enter to use the default (1). ");
		input = reader.readLine();
		int threadCount = 1;
		if( StringUtils.isBlank(input) ) {
			System.out.println("Using default of 1 thread.");
		}
		else {
			threadCount = Integer.valueOf(input);
			System.out.println("Using your chosen thread count: " + threadCount);
		}
		
		String endPlainText = null;
		
		List<EnigmaBreaker> enigmaBreakers = new ArrayList<EnigmaBreaker>();
		for( int index=0; index<threadCount; index++ ) {
			EnigmaBreaker enigmaBreaker = new EnigmaBreaker(cipherText, steckerPairCount);
			enigmaBreakers.add( enigmaBreaker );
			enigmaBreaker.start();
		}
		
		char[] solvedPositions = new char[3];
		List<SteckerCable> solvedSteckeredPairs;
		
		while(true) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			EnigmaBreaker stoppedEnigmaBreaker = null;
			for( EnigmaBreaker enigmaBreaker : enigmaBreakers ) {
				if( !enigmaBreaker.isAlive() ) {
					stoppedEnigmaBreaker = enigmaBreaker;
					break;
				}
			}
			
			if( stoppedEnigmaBreaker==null )
				System.out.println("~~~~~~~~~~~ all threads are still running...");
			else {
				// TODO: we can't just check that a thread has stopped, we have to check if it actually returned a result
				// TODO: toString() and Equals based on index 1 of 4, etc.
				System.out.println("~~~~~~~~~~~ thread " + stoppedEnigmaBreaker + " has returned with results! interrupting other threads now.");
				
				for( EnigmaBreaker enigmaBreaker : enigmaBreakers ) { // first interrupt any remaining live threads
					if( enigmaBreaker.isAlive() )
						enigmaBreaker.interrupt();
				}
				
				// then pull the solved positions info from the thread that returned successfully
				System.out.println("~~~~~~~~~~~ solvedPositions: " + Arrays.toString(stoppedEnigmaBreaker.getSolvedPositions()));
				solvedPositions = stoppedEnigmaBreaker.getSolvedPositions();
				System.out.println("~~~~~~~~~~~ solvedSteckeredPairs: " + stoppedEnigmaBreaker.getSolvedSteckeredPairs());
				solvedSteckeredPairs = stoppedEnigmaBreaker.getSolvedSteckeredPairs();
				break;
			}
		}
		
		enigmaMachine.setRotors(solvedPositions);
		enigmaMachine.setSteckers(solvedSteckeredPairs);
		
		endPlainText = enigmaMachine.decrypt(cipherText);
		System.out.println("~~~~~~~~~~~ End plain text: " + endPlainText);
	}
	
}
