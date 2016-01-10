package com.demo.crypto.enigma;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.demo.crypto.enigma.exception.NoMatchingCribException;
import com.demo.crypto.enigma.model.EnigmaMachine;
import com.demo.crypto.enigma.model.SteckerCable;

public class EnigmaBreakerTest {
	
	// this could be any valid positions, but for the sake of a thorough test that exercises the most code, choosing { Z, Z, Z }
	private static final char[] TEST_INITIAL_POSITIONS = new char[]{ 'Z', 'Z', 'Z' };
	
	private static final String TEST_BREAKABLE_PLAIN_TEXT = "ANXGENERALHELLOWORLD";
	private static final String TEST_UNBREAKABLE_PLAIN_TEXT = "HELLO";
	
	
	@Test( expected=NoMatchingCribException.class )
	public void testDecryptNoCrib() throws NoMatchingCribException {
		EnigmaMachine enigmaMachine = new EnigmaMachine( TEST_INITIAL_POSITIONS );
		
		EnigmaBreaker.decrypt( enigmaMachine.encrypt(TEST_UNBREAKABLE_PLAIN_TEXT) );
	}
	
	@Test
	public void testDecryptUnsteckered() throws NoMatchingCribException {
		EnigmaMachine enigmaMachine = new EnigmaMachine( TEST_INITIAL_POSITIONS );
		
		assertEquals( TEST_BREAKABLE_PLAIN_TEXT, EnigmaBreaker.decrypt( enigmaMachine.encrypt(TEST_BREAKABLE_PLAIN_TEXT) ) );
	}
	
	@Test
	public void testDecryptOneSteckerPair() throws NoMatchingCribException {
		List<SteckerCable> steckeredPairs = new ArrayList<SteckerCable>();
		// TODO: our crib drag doesn't work where steckered chars are in the text but not in the crib portion
		steckeredPairs.add( new SteckerCable('B', 'E') );
		
		EnigmaMachine enigmaMachine = new EnigmaMachine( TEST_INITIAL_POSITIONS, steckeredPairs );
		
		assertEquals( TEST_BREAKABLE_PLAIN_TEXT, EnigmaBreaker.decrypt( enigmaMachine.encrypt(TEST_BREAKABLE_PLAIN_TEXT), steckeredPairs.size() ) );
	}
	
	
	@Test
	public void testDecryptTwoSteckerPair() throws NoMatchingCribException {
		List<SteckerCable> steckeredPairs = new ArrayList<SteckerCable>();
		steckeredPairs.add( new SteckerCable('B', 'E') );
		steckeredPairs.add( new SteckerCable('L', 'Z') );
		
		EnigmaMachine enigmaMachine = new EnigmaMachine( TEST_INITIAL_POSITIONS, steckeredPairs );
		
		assertEquals( TEST_BREAKABLE_PLAIN_TEXT, EnigmaBreaker.decrypt( enigmaMachine.encrypt(TEST_BREAKABLE_PLAIN_TEXT), steckeredPairs.size() ) );
	}
	/*
	@Test
	public void testDecryptThreeSteckerPair() throws NoMatchingCribException {
		List<SteckerCable> steckeredPairs = new ArrayList<SteckerCable>();
		steckeredPairs.add( new SteckerCable('A', 'E') );
		steckeredPairs.add( new SteckerCable('L', 'Z') );
		steckeredPairs.add( new SteckerCable('H', 'R') );
		
		EnigmaMachine enigmaMachine = new EnigmaMachine( TEST_INITIAL_POSITIONS, steckeredPairs );
		
		assertEquals( "HELLOWORLDIAMSAM", EnigmaBreaker.decrypt( enigmaMachine.encrypt("HELLOWORLDIAMSAM"), steckeredPairs.size() ) );
	}
	
	@Test
	public void testDecryptFourSteckerPair() throws NoMatchingCribException {
		List<SteckerCable> steckeredPairs = new ArrayList<SteckerCable>();
		steckeredPairs.add( new SteckerCable('B', 'E') );
		steckeredPairs.add( new SteckerCable('L', 'Z') );
		steckeredPairs.add( new SteckerCable('H', 'R') );
		steckeredPairs.add( new SteckerCable('O', 'X') );
		
		EnigmaMachine enigmaMachine = new EnigmaMachine( TEST_INITIAL_POSITIONS, steckeredPairs );
		
		assertEquals( "HELLOWORLDIAMSAM", EnigmaBreaker.decrypt( enigmaMachine.encrypt("HELLOWORLDIAMSAM"), steckeredPairs.size() ) );
	}
	*/
}
