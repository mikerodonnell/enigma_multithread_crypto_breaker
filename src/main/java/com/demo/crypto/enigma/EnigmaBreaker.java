package com.demo.crypto.enigma;

import java.util.Arrays;
import java.util.List;

import com.demo.crypto.enigma.exception.DuplicateSteckerException;
import com.demo.crypto.enigma.exception.NoMatchingCribException;
import com.demo.crypto.enigma.model.EnigmaMachine;
import com.demo.crypto.enigma.model.SteckerCable;
import com.demo.crypto.enigma.model.crib.Crib;
import com.demo.crypto.enigma.util.Alphabet;
import com.demo.crypto.enigma.util.CribDragger;
import com.demo.crypto.enigma.util.SteckerCombinationTracker;

public class EnigmaBreaker extends Thread {
	
	private String cipherText;
	private int steckerPairCount;
	private int parallelThreadIndex = 0;
	private int parallelThreadCount = 1; // valid values: 1, 2, 4, 8
	private int slowRotorStartIndex = 0; // default to the single-thread case where there's 1 EnigmaBreaker that needs to cover A ...
	private int slowRotorEndIndex = 25;  // through Z
	
	private char[] solvedPositions = null;
	private List<SteckerCable> solvedSteckeredPairs = null;
	
	public EnigmaBreaker( final String cipherText ) {
		this( cipherText, SteckerCombinationTracker.DEFAULT_STECKER_PAIR_COUNT );
	}
	
	public EnigmaBreaker( final String cipherText, int steckerPairCount ) {
		this( cipherText, steckerPairCount, 0, 1 );
	}
	
	public EnigmaBreaker( final String cipherText, int steckerPairCount, int parallelThreadIndex, int parallelThreadCount ) {
		this.cipherText = cipherText;
		this.steckerPairCount = steckerPairCount;
		this.parallelThreadIndex = parallelThreadIndex;
		this.parallelThreadCount = parallelThreadCount;
		
		if(parallelThreadCount==2) {
			if(parallelThreadIndex==0) {
				slowRotorEndIndex = 12; 
			}
			else {
				slowRotorStartIndex = 13;
			}
		}
		else if(parallelThreadCount==4) {
			if(parallelThreadIndex==0) {
				slowRotorEndIndex = 6;
			}
			else if(parallelThreadIndex==1) {
				slowRotorStartIndex = 7;
				slowRotorEndIndex = 13;
			}
			else if(parallelThreadIndex==2) {
				slowRotorStartIndex = 14;
				slowRotorEndIndex = 19;
			}
			else {
				slowRotorStartIndex = 20;
			}
		}
		else if(parallelThreadCount==8) {
			if(parallelThreadIndex==0) {
				slowRotorEndIndex = 3;
			}
			else if(parallelThreadIndex==1) {
				slowRotorStartIndex = 4;
				slowRotorEndIndex = 7;
			}
			else if(parallelThreadIndex==2) {
				slowRotorStartIndex = 8;
				slowRotorEndIndex = 10;
			}
			else if(parallelThreadIndex==3) {
				slowRotorStartIndex = 11;
				slowRotorEndIndex = 13;
			}
			else if(parallelThreadIndex==4) {
				slowRotorStartIndex = 14;
				slowRotorEndIndex = 16;
			}
			else if(parallelThreadIndex==5) {
				slowRotorStartIndex = 17;
				slowRotorEndIndex = 19;
			}
			else if(parallelThreadIndex==6) {
				slowRotorStartIndex = 20;
				slowRotorEndIndex = 22;
			}
			else {
				slowRotorStartIndex = 23;
			}
		}
	}
	
	@Override
	public void run() {
		decrypt();
	}
	
	
	/**
	 * Decrypt the given cipher text using a crib-drag attack. The count of stecker pairs must also be specified. The full set of 10 stecker pairs Enigma historically used
	 * is supported, but for testing purposes it's usually desirable to use fewer to expedite testing.
	 * 
	 * @param cipherText
	 * @param steckerPairCount the number of stecker pairs used in the Enigma machine. regardless of this value, an unsteckered configuration is always attempted first.
	 * @return the plain text corresponding to the given cipher text.
	 * @throws NoMatchingCribException 
	 * @throws InterruptedException 
	 */
	public void decrypt() {
		log("decrypting cipher text: " + cipherText);
		
		Crib crib = null;
		try {
			crib = (new CribDragger()).getCribForMessage( cipherText );
		}
		catch( NoMatchingCribException NoMatchingCribException ) {
			log("none of our known cribs are a possible match for this cipher text. exiting.");
			return;
		}
		
		log("using crib: " + crib + " starting at index " + crib.getStartIndex());
		final EnigmaMachine enigmaMachine = new EnigmaMachine();
		
		char[] rotorPositions = new char[3];
		
		String substring = cipherText.substring(crib.getStartIndex(), crib.getPlainText().length); // the first X characters of the cipher text.
		final char[] substringArray = substring.toCharArray();
		
		SteckerCombinationTracker steckerCombinationTracker = new SteckerCombinationTracker(steckerPairCount);
		
		while( !interrupted() && steckerCombinationTracker.hasNext() ) {
			List<SteckerCable> steckeredPairs = steckerCombinationTracker.next();
			try {
				enigmaMachine.setSteckers(steckeredPairs);
			}
			catch( DuplicateSteckerException duplicateSteckerException ) {
				// skip this stecker combination, it's.invalid. example: [C => E, E => Z]
				continue;
			}
			
			if( steckeredPairs.isEmpty() )
				log("attempting to break with no steckers");
			else if( steckerCombinationTracker.getCombinationsCount() < 3 || steckerCombinationTracker.getCombinationsCount() % 2500 == 0 )
				log("still working ... currently attempting steckers: " + steckeredPairs + ", and rotor range " + Alphabet.ALPHABET_ARRAY[slowRotorStartIndex] + " through " + Alphabet.ALPHABET_ARRAY[slowRotorEndIndex]);
			
			// attempt to break with this particular stecker configuration by iterating through every possible rotor configuration.
			for( int slowRotorIndex=slowRotorStartIndex; slowRotorIndex<=slowRotorEndIndex; slowRotorIndex++ ) {
				rotorPositions[0] = Alphabet.ALPHABET_ARRAY[slowRotorIndex];
				
				for( int middleRotorIndex=0; middleRotorIndex<26; middleRotorIndex++ ) {
					rotorPositions[1] = Alphabet.ALPHABET_ARRAY[middleRotorIndex];
					
					for( int fastRotorIndex=0; fastRotorIndex<26; fastRotorIndex++ ) {
						rotorPositions[2] = Alphabet.ALPHABET_ARRAY[fastRotorIndex];
						
						enigmaMachine.setRotors(rotorPositions);
						
						if( isMatchWithSettings(crib.getPlainText(), substringArray, enigmaMachine) ) {
							// we found a match!! record the correct configuration to member variables so that EnigmaBreakerControl can retrieve them via #getSolvedPositions() and 
							// #getSolvedSteckerPairs() then, EnigmaBreakerControl can decrypt!
							log("encryption key found!! steckers: " + steckeredPairs + "; rotor positions: " + Arrays.toString(rotorPositions));
							
							this.solvedPositions = rotorPositions;
							this.solvedSteckeredPairs = steckeredPairs;
							return; // all done, return asap
						}
					}
				}
			}
		}
	}
	
	/**
	 * determine if the given cipherTextSegment decrypts to the given crib when decrypted with the given EnigmaMachine state.
	 * 
	 * @param crib the plaintext of a phrase that is known to appear somewhere in an encrypted message.
	 * @param cipherTextSegment a segment of encrypted text that is the same length as the crib, which may or may not match the crib when decrypted.
	 * @param enigmaMachine
	 * @return
	 */
	private static boolean isMatchWithSettings( char[] crib, char[] cipherTextSegment, final EnigmaMachine enigmaMachine ) {
		for( int index=0; index<crib.length; index++ ) {
			if( crib[index] != enigmaMachine.decrypt(cipherTextSegment[index]) )
				return false;
		}
		
		return true;
	}
	
	
	public char[] getSolvedPositions() {
		return this.solvedPositions;
	}
	
	public List<SteckerCable> getSolvedSteckeredPairs() {
		return this.solvedSteckeredPairs;
	}
	
	private void log( final String message ) {
		System.out.println(this + ": " + message);
	}
	
	@Override
	public String toString() {
		return "thread " + (parallelThreadIndex+1) + " of " + parallelThreadCount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + parallelThreadIndex;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		
		if( obj!=null && obj instanceof EnigmaBreaker ) {
			EnigmaBreaker other = (EnigmaBreaker) obj;
			return (this.parallelThreadIndex == other.parallelThreadIndex);
		}
		
		return false;
	}
}
