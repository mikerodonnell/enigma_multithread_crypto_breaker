package com.demo.crypto.enigma.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.demo.crypto.enigma.util.Alphabet;

public class EnigmaIRotor1Test {

	public AbstractEnigmaRotor rotor;
	
	
	@Before
	public void setupTestCase() {
		AbstractEnigmaRotor leftRotor = new EnigmaIRotor3(null);
		rotor = new EnigmaIRotor1( leftRotor ); // for this unit test, it doesn't actually matter what type of rotor we place to the left of our EnigmaIRotor2.
	}
	
	
	@Test
	public void testStep() {
		assertEquals( 0, rotor.getOffset() );
		assertEquals( 0, rotor.getLeftRotor().getOffset() );
		
		for( int stepCount=0; stepCount<Alphabet.indexOf('Q'); stepCount++ ) // march along to the letter E
			rotor.step();
		
		assertEquals( 0, rotor.getLeftRotor().getOffset() ); // verify just before we move past the letter Q that the left rotor is still at 0
		rotor.step(); // and do one more step ...
		assertEquals( 1, rotor.getLeftRotor().getOffset() ); // ... and verify that the left rotor has moved
		
		for( int stepCount=0; stepCount<(24 - Alphabet.indexOf('Q')); stepCount++ ) // go the rest of the way to the end of the alphabet
			rotor.step();
		
		assertEquals( 25, rotor.getOffset() ); // now verify the edge rotor index rolls over from 25 to 0
		rotor.step();
		assertEquals( 0, rotor.getOffset() );
	}
	
}
