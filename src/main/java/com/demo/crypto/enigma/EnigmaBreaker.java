package com.demo.crypto.enigma;

import java.util.Map;

import com.demo.crypto.enigma.model.EnigmaMachine;
import com.demo.crypto.enigma.model.util.Alphabet;
import com.demo.crypto.enigma.model.util.SteckerPairTracker;

public class EnigmaBreaker {
	
	public static String decrypt( final String cipherText ) {
		System.out.println( "~~~~~~~ decrypting cipher text: " + cipherText );
		
		final String startingCrib = "HELLO";
		final char[] startingCribArray = startingCrib.toCharArray();
		
		final EnigmaMachine enigmaMachine = new EnigmaMachine();
		
		char[] initialPositions = new char[3];
		
		String substring = cipherText.substring(0, startingCrib.length()); // the first 5 characters of the cipher text, see if it corresponds to the crib
		final char[] substringArray = substring.toCharArray();
		
		SteckerPairTracker steckerPairTracker = new SteckerPairTracker();
		
		while( steckerPairTracker.hasNext() ) {
			Map<Character, Character> steckeredPairs = steckerPairTracker.next();
			// attempt to break with this particular stecker configuration by iterating through every possible rotor configuration.
			
			for( int slowRotorIndex=0; slowRotorIndex<26; slowRotorIndex++ ) {
				initialPositions[0] = Alphabet.ALPHABET_ARRAY[slowRotorIndex];
				
				for( int middleRotorIndex=0; middleRotorIndex<26; middleRotorIndex++ ) {
					initialPositions[1] = Alphabet.ALPHABET_ARRAY[middleRotorIndex];
					
					for( int fastRotorIndex=0; fastRotorIndex<26; fastRotorIndex++ ) {
						initialPositions[2] = Alphabet.ALPHABET_ARRAY[fastRotorIndex];
						
						enigmaMachine.set(initialPositions, steckeredPairs);
						
						if( isMatchWithSettings(startingCribArray, substringArray, enigmaMachine) ) {
							// we found a match!!
							// now that we know the correct settings, we need to set the machine back to those settings since each test pass alters the state. then,
							// we can decrypt!
							enigmaMachine.set(initialPositions, steckeredPairs);
							
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
