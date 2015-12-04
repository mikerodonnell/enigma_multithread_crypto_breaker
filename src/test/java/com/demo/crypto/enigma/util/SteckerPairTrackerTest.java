package com.demo.crypto.enigma.util;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.demo.crypto.enigma.model.util.Alphabet;
import com.demo.crypto.enigma.model.util.SteckerPairTracker;

public class SteckerPairTrackerTest {

	@Test
	public void testNext() {
		SteckerPairTracker steckerPairTracker = new SteckerPairTracker();
		
		Map<Character, Character> firstPositions = new HashMap<Character, Character>();
		
		assertEquals( firstPositions, steckerPairTracker.next() );
		for( int originalIndex=0; originalIndex<26; originalIndex++ ) {
			for( int replacementIndex=originalIndex+1; replacementIndex<26; replacementIndex++ ) {
				firstPositions.clear();
				firstPositions.put(Alphabet.ALPHABET_ARRAY[originalIndex], Alphabet.ALPHABET_ARRAY[replacementIndex] );
				assertEquals( firstPositions, steckerPairTracker.next() );
			}
			
		}
	}
}
