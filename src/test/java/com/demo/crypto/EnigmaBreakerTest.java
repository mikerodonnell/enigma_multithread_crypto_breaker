package com.demo.crypto;

import static org.junit.Assert.*;

import org.junit.Test;

import com.demo.crypto.enigma.EnigmaBreaker;
import com.demo.crypto.enigma.model.EnigmaMachine;

public class EnigmaBreakerTest {
	
	@Test
	public void testDecrypt() {
		char[] randomInitialPositions = new char[]{ 'Z', 'Z', 'Z' };
		EnigmaMachine enigmaMachine = new EnigmaMachine( randomInitialPositions );
		
		assertEquals( "HELLOWORLD", EnigmaBreaker.decrypt( enigmaMachine.encrypt("HELLOWORLD") ) );
	}
}
