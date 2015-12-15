package com.demo.crypto.enigma;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.demo.crypto.enigma.model.EnigmaMachine;
import com.demo.crypto.enigma.model.SteckerCable;

public class EnigmaBreakerTest {
	
	@Test
	public void testDecryptUnsteckered() {
		char[] randomInitialPositions = new char[]{ 'Z', 'Z', 'Z' };
		EnigmaMachine enigmaMachine = new EnigmaMachine( randomInitialPositions );
		
		assertEquals( "HELLOWORLDIAMSAM", EnigmaBreaker.decrypt( enigmaMachine.encrypt("HELLOWORLDIAMSAM") ) );
	}
	
	
	@Test
	public void testDecrypt() {
		char[] randomInitialPositions = new char[]{ 'Z', 'Z', 'Z' };
		
		List<SteckerCable> steckeredPairs = new ArrayList<SteckerCable>();
		steckeredPairs.add( new SteckerCable('C', 'E') );
		steckeredPairs.add( new SteckerCable('L', 'Z') ); // TODO: our crib drag doesnt work where the crib includes 0 stecker chars (out of 20 total)
		// TODO: more pairs
		
		EnigmaMachine enigmaMachine = new EnigmaMachine( randomInitialPositions, steckeredPairs );
		
		assertEquals( "HELLOWORLDIAMSAM", EnigmaBreaker.decrypt( enigmaMachine.encrypt("HELLOWORLDIAMSAM") ) );
	}
	
}
