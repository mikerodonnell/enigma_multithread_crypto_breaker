package com.demo.crypto.enigma;

import java.util.Arrays;
import java.util.List;

import com.demo.crypto.enigma.model.EnigmaMachine;
import com.demo.crypto.enigma.model.SteckerCable;
import com.demo.crypto.enigma.model.exception.DuplicateSteckerException;
import com.demo.crypto.enigma.model.util.Alphabet;
import com.demo.crypto.enigma.model.util.SteckerCombinationTracker;

public class EnigmaBreaker {
	
	public static String decrypt( final String cipherText ) {
		return decrypt(cipherText, SteckerCombinationTracker.DEFAULT_STECKER_PAIR_COUNT);
	}
	
	/**
	 * Decrypt the given cipher text using a crib-drag attack. The count of stecker pairs must also be specified. The full set of 10 stecker pairs Enigma historically used
	 * is supported, but for testing purposes it's usually desirable to use fewer to expedite testing.
	 * 
	 * @param cipherText
	 * @param steckerPairCount the number of stecker pairs used in the Enigma machine. regardless of this value, an unsteckered configuration is always attempted first.
	 * @return the plain text corresponding to the given cipher text.
	 */
	public static String decrypt( final String cipherText, int steckerPairCount ) {
		System.out.println( "~~~~~~~ decrypting cipher text: " + cipherText );
		
		final String startingCrib = "HELLOWORLD";
		final char[] startingCribArray = startingCrib.toCharArray();
		
		final EnigmaMachine enigmaMachine = new EnigmaMachine();
		
		char[] initialPositions = new char[3];
		
		String substring = cipherText.substring(0, startingCrib.length()); // the first 10 characters of the cipher text, see if it corresponds to the crib
		final char[] substringArray = substring.toCharArray();
		
		SteckerCombinationTracker steckerCombinationTracker = new SteckerCombinationTracker(steckerPairCount);
		
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
			else if( steckerCombinationTracker.getCombinationsCount() % 1000 == 0 )
				System.out.println("~~~~~~~~~~~ still working ... currently attempting to break with steckers: " + steckeredPairs);
			
			// attempt to break with this particular stecker configuration by iterating through every possible rotor configuration.
			for( int slowRotorIndex=0; slowRotorIndex<26; slowRotorIndex++ ) {
				initialPositions[0] = Alphabet.ALPHABET_ARRAY[slowRotorIndex];
				
				for( int middleRotorIndex=0; middleRotorIndex<26; middleRotorIndex++ ) {
					initialPositions[1] = Alphabet.ALPHABET_ARRAY[middleRotorIndex];
					
					for( int fastRotorIndex=0; fastRotorIndex<26; fastRotorIndex++ ) {
						initialPositions[2] = Alphabet.ALPHABET_ARRAY[fastRotorIndex];
						
						enigmaMachine.setRotors(initialPositions);
						
						if( isMatchWithSettings(startingCribArray, substringArray, enigmaMachine) ) {
							// we found a match!!
							// now that we know the correct settings, we need to set the rotors back to the last settings we tested, since each test pass turns the rotors.
							// the steckers are fine; they don't move around each time we encipher a letter like the rotors do.
							// then, we can decrypt!
							
							System.out.println("~~~~~~~~~~~ MATCH! steckers: " + steckeredPairs + ", rotor positions: " + Arrays.toString(initialPositions));
							enigmaMachine.setRotors(initialPositions);
							
							return enigmaMachine.decrypt(cipherText);
						}
					}
				}
			}
		}
		
		return "ERROR -- NOT FOUND!";
	}
	
	
	private static boolean isMatchWithSettings( char[] crib, char[] testString, EnigmaMachine enigmaMachine ) {
		
		for( int index=0; index<crib.length; index++ ) {
			if( crib[index] != enigmaMachine.decrypt(testString[index]) )
				return false;
		}
		
		return true;
	}
}
