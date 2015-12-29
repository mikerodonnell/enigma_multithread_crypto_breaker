package com.demo.crypto.enigma;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.demo.crypto.enigma.model.EnigmaMachine;
import com.demo.crypto.enigma.model.SteckerCable;

public class EnigmaBreakerTest {
	
	// this could be any valid positions, but for the sake of a thorough test that exercises the most code, choosing { Z, Z, Z }
	private static final char[] TEST_INITIAL_POSITIONS = new char[]{ 'Z', 'Z', 'Z' };
	
	@Test
	public void testDecryptUnsteckered() {
		EnigmaMachine enigmaMachine = new EnigmaMachine( TEST_INITIAL_POSITIONS );
		
		assertEquals( "HELLOWORLDIAMSAM", EnigmaBreaker.decrypt( enigmaMachine.encrypt("HELLOWORLDIAMSAM") ) );
	}
	
	@Test
	public void testDecryptOneSteckerPair() {
		List<SteckerCable> steckeredPairs = new ArrayList<SteckerCable>();
		// TODO: our crib drag doesn't work where steckered chars are in the text but not in the crib portion
		steckeredPairs.add( new SteckerCable('B', 'E') );
		
		EnigmaMachine enigmaMachine = new EnigmaMachine( TEST_INITIAL_POSITIONS, steckeredPairs );
		
		assertEquals( "HELLOWORLDIAMSAM", EnigmaBreaker.decrypt( enigmaMachine.encrypt("HELLOWORLDIAMSAM"), steckeredPairs.size() ) );
	}
	
	@Test
	public void testDecryptTwoSteckerPair() {
		List<SteckerCable> steckeredPairs = new ArrayList<SteckerCable>();
		steckeredPairs.add( new SteckerCable('B', 'E') );
		steckeredPairs.add( new SteckerCable('L', 'Z') );
		
		EnigmaMachine enigmaMachine = new EnigmaMachine( TEST_INITIAL_POSITIONS, steckeredPairs );
		
		assertEquals( "HELLOWORLDIAMSAM", EnigmaBreaker.decrypt( enigmaMachine.encrypt("HELLOWORLDIAMSAM"), steckeredPairs.size() ) );
	}
	/*
	@Test
	public void testDecryptThreeSteckerPair() {
		List<SteckerCable> steckeredPairs = new ArrayList<SteckerCable>();
		steckeredPairs.add( new SteckerCable('A', 'E') );
		steckeredPairs.add( new SteckerCable('L', 'Z') );
		steckeredPairs.add( new SteckerCable('H', 'R') );
		
		EnigmaMachine enigmaMachine = new EnigmaMachine( TEST_INITIAL_POSITIONS, steckeredPairs );
		
		assertEquals( "HELLOWORLDIAMSAM", EnigmaBreaker.decrypt( enigmaMachine.encrypt("HELLOWORLDIAMSAM"), steckeredPairs.size() ) );
	}
	
	@Test
	public void testDecryptFourSteckerPair() {
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
