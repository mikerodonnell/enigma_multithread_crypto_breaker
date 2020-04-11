package com.demo.crypto.enigma.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AbstractEnigmaRotorTest {

	private AbstractEnigmaRotor rotor;

	@BeforeEach
	public void setupTestCase() {
		rotor = new EnigmaIRotor3(null); // any instantiation of AbstractEnigmaRotor will suffice for this test.
	}

	@Test
	public void testGetOutputIndex() {

		assertEquals(1, rotor.getOutputIndex(0));
		assertEquals(3, rotor.getOutputIndex(1));
		assertEquals(5, rotor.getOutputIndex(2));
		assertEquals(25, rotor.getOutputIndex(12));
		assertEquals(14, rotor.getOutputIndex(25));

		rotor.step();
		assertEquals(2, rotor.getOutputIndex(0));
		assertEquals(4, rotor.getOutputIndex(1));
		assertEquals(6, rotor.getOutputIndex(2));
		assertEquals(8, rotor.getOutputIndex(3));
		assertEquals(0, rotor.getOutputIndex(25));

		rotor.step();
		for (int i = 0; i < 26; i++) {
			int outputIndex = rotor.getOutputIndex(i);
			assertTrue(outputIndex >= 0);
			assertTrue(outputIndex <= 25);
		}
	}

	@Test
	public void testGetOutputIndexInverse() {
		// for every index (letter of the alphabet), verify getOutputIndexInverse() does in fact return the inverse of getOutputIndex().
		for (int i = 0; i < 26; i++)
			assertEquals(i, rotor.getOutputIndexInverse(rotor.getOutputIndex(i)));

		rotor.step(); // now step the rotor a few times ...
		rotor.step();
		rotor.step();

		// ... and repeat the test
		for (int i = 0; i < 26; i++)
			assertEquals(i, rotor.getOutputIndexInverse(rotor.getOutputIndex(i)));
	}
}
