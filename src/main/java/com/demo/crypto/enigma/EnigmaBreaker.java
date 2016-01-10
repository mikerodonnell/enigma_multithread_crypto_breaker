package com.demo.crypto.enigma;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.demo.crypto.enigma.exception.DuplicateSteckerException;
import com.demo.crypto.enigma.exception.NoMatchingCribException;
import com.demo.crypto.enigma.model.EnigmaMachine;
import com.demo.crypto.enigma.model.SteckerCable;
import com.demo.crypto.enigma.model.crib.Crib;
import com.demo.crypto.enigma.util.Alphabet;
import com.demo.crypto.enigma.util.ConfigurationUtil;
import com.demo.crypto.enigma.util.CribDragger;
import com.demo.crypto.enigma.util.SteckerCombinationTracker;

public class EnigmaBreaker {
	
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
		System.out.print("Now, enter a sequence of starting rotor positions, from left rotor to right. for example: ABC. Or, press enter for random positions.");
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
		System.out.println("Encrypting the chosen plain text with rotor positions " + Arrays.toString(initialPositions) + "and steckered pairs: " + steckeredPairs);
		
		EnigmaMachine enigmaMachine = new EnigmaMachine(initialPositions, steckeredPairs);
		String cipherText = enigmaMachine.encrypt(startPlainText);
		System.out.println("We encrypted your plain text " + startPlainText + " to " + cipherText + ", let's see if we can decrypt it back now by re-discovering the rotor and stecker settings.");
		
		String endPlainText = null;
		try {
			endPlainText = decrypt(cipherText);
			System.out.println("Result: ");
			System.out.println(endPlainText);
		}
		catch(NoMatchingCribException noMatchingCribException) {
			System.out.println("Unable to decrypt this message. No known crib matches the message.");
		}
	}
	
	public static String decrypt( final String cipherText ) throws NoMatchingCribException {
		return decrypt(cipherText, SteckerCombinationTracker.DEFAULT_STECKER_PAIR_COUNT);
	}
	
	/**
	 * Decrypt the given cipher text using a crib-drag attack. The count of stecker pairs must also be specified. The full set of 10 stecker pairs Enigma historically used
	 * is supported, but for testing purposes it's usually desirable to use fewer to expedite testing.
	 * 
	 * @param cipherText
	 * @param steckerPairCount the number of stecker pairs used in the Enigma machine. regardless of this value, an unsteckered configuration is always attempted first.
	 * @return the plain text corresponding to the given cipher text.
	 * @throws NoMatchingCribException 
	 */
	public static String decrypt( final String cipherText, int steckerPairCount ) throws NoMatchingCribException {
		System.out.println( "~~~~~~~ decrypting cipher text: " + cipherText );
		
		final Crib crib = CribDragger.getCribForMessage( cipherText ); // throws NoMatchingCribException, let it bubble up
		
		System.out.println( "~~~~~~~ using crib: " + crib );
		String plainText = null;
		final EnigmaMachine enigmaMachine = new EnigmaMachine();
		
		char[] initialPositions = new char[3];
		
		String substring = cipherText.substring(0, crib.getPlainText().length); // the first X characters of the cipher text.
		final char[] substringArray = substring.toCharArray();
		
		SteckerCombinationTracker steckerCombinationTracker = new SteckerCombinationTracker(steckerPairCount);
		
		settingsSearch: 
		while( steckerCombinationTracker.hasNext() ) {
			List<SteckerCable> steckeredPairs = steckerCombinationTracker.next();
			try {
				enigmaMachine.setSteckers(steckeredPairs);
			}
			catch( DuplicateSteckerException duplicateSteckerException ) {
				// skip this stecker combination, it's.invalid. example: [C => E, E => Z]
				continue;
			}
			
			if( steckeredPairs.isEmpty() )
				System.out.println("~~~~~~~~~~~ attempting to break with no steckers");
			else if( steckerCombinationTracker.getCombinationsCount() < 3 || steckerCombinationTracker.getCombinationsCount() % 2000 == 0 )
				System.out.println("~~~~~~~~~~~ still working ... currently attempting to break with steckers: " + steckeredPairs);
			
			// attempt to break with this particular stecker configuration by iterating through every possible rotor configuration.
			for( int slowRotorIndex=0; slowRotorIndex<26; slowRotorIndex++ ) {
				initialPositions[0] = Alphabet.ALPHABET_ARRAY[slowRotorIndex];
				
				for( int middleRotorIndex=0; middleRotorIndex<26; middleRotorIndex++ ) {
					initialPositions[1] = Alphabet.ALPHABET_ARRAY[middleRotorIndex];
					
					for( int fastRotorIndex=0; fastRotorIndex<26; fastRotorIndex++ ) {
						initialPositions[2] = Alphabet.ALPHABET_ARRAY[fastRotorIndex];
						
						enigmaMachine.setRotors(initialPositions);
						
						if( isMatchWithSettings(crib.getPlainText(), substringArray, enigmaMachine) ) {
							// we found a match!!
							// now that we know the correct settings, we need to set the rotors back to the last settings we tested, since each test pass turns the rotors.
							// the steckers are fine; they don't move around each time we encipher a letter like the rotors do.
							// then, we can decrypt!
							
							System.out.println("~~~~~~~~~~~ MATCH! steckers: " + steckeredPairs + ", rotor positions: " + Arrays.toString(initialPositions));
							enigmaMachine.setRotors(initialPositions);
							
							plainText = enigmaMachine.decrypt(cipherText);
							break settingsSearch;
						}
					}
				}
			}
		}
		
		return plainText;
	}
	
	
	private static boolean isMatchWithSettings( char[] crib, char[] testString, EnigmaMachine enigmaMachine ) {
		
		for( int index=0; index<crib.length; index++ ) {
			if( crib[index] != enigmaMachine.decrypt(testString[index]) )
				return false;
		}
		
		return true;
	}
}
