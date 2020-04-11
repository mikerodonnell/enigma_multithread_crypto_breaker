package com.demo.crypto.enigma.model;

import com.demo.crypto.enigma.util.Alphabet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnigmaIRotor3Test {

	public AbstractEnigmaRotor rotor;

	@BeforeEach
	public void setupTestCase() {
		AbstractEnigmaRotor leftRotor = new EnigmaIRotor2(null);
		rotor = new EnigmaIRotor3(leftRotor); // for this unit test, it doesn't actually matter what type of rotor we place to the left of our EnigmaIRotor3.
	}

	@Test
	public void testStep() {
		assertEquals(0, rotor.getOffset());
		assertEquals(0, rotor.getLeftRotor().getOffset());

		for (int stepCount = 0; stepCount < Alphabet.indexOf('V'); stepCount++) // march along to the letter V
			rotor.step();

		assertEquals(0, rotor.getLeftRotor().getOffset()); // verify just before we move past the letter V that the left rotor is still at 0
		rotor.step(); // and do one more step ...
		assertEquals(1, rotor.getLeftRotor().getOffset()); // ... and verify that the left rotor has moved

		for (int stepCount = 0; stepCount < (24 - Alphabet.indexOf('V')); stepCount++) // go the rest of the way to the end of the alphabet
			rotor.step();

		assertEquals(25, rotor.getOffset()); // now verify the edge rotor index rolls over from 25 to 0
		rotor.step();
		assertEquals(0, rotor.getOffset());
	}
}
