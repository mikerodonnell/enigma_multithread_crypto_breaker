package com.demo.crypto.enigma;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.demo.crypto.enigma.EnigmaBreaker;
import com.demo.crypto.enigma.model.EnigmaMachine;

public class EnigmaBreakerTest {
	
	@Test
	public void testDecryptUnsteckered() {
		char[] randomInitialPositions = new char[]{ 'Z', 'Z', 'Z' };
		EnigmaMachine enigmaMachine = new EnigmaMachine( randomInitialPositions );
		
		assertEquals( "HELLOWORLD", EnigmaBreaker.decrypt( enigmaMachine.encrypt("HELLOWORLD") ) );
	}
	
	
	@Test
	public void testDecrypt() {
		char[] randomInitialPositions = new char[]{ 'Z', 'Z', 'Z' };
		
		Map<Character, Character> steckeredPairs = new HashMap<Character, Character>();
		steckeredPairs.put( 'L', 'N' );
		// TODO: more pairs
		
		EnigmaMachine enigmaMachine = new EnigmaMachine( randomInitialPositions, steckeredPairs );
		
		assertEquals( "HELLOWORLD", EnigmaBreaker.decrypt( enigmaMachine.encrypt("HELLOWORLD") ) );
	}
	
}
