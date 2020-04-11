package com.demo.crypto.enigma.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnigmaMachineTest {

	@Test
	public void testEncryptDefaultPositions() {

		EnigmaMachine enigmaMachine = new EnigmaMachine();

		// this is a known documented example, when the rotors start in AAA position with no ring offset and no plugboard
		assertEquals("BDZGO", enigmaMachine.encrypt("AAAAA"));

		// these are sample encryptions for other implementations, trusting that they're correct
		enigmaMachine.reset();
		assertEquals("BJELRQZVJWARXSNBXORSTNCFME", enigmaMachine.encrypt("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		enigmaMachine.reset();
		assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", enigmaMachine.decrypt("BJELRQZVJWARXSNBXORSTNCFME"));

		enigmaMachine.reset();
		assertEquals("ILBDAAMTAZ", enigmaMachine.encrypt("HELLOWORLD"));
		enigmaMachine.reset();
		assertEquals("HELLOWORLD", enigmaMachine.decrypt("ILBDAAMTAZ"));
	}

	@Test
	public void testEncryptUnsteckered() {

		EnigmaMachine enigmaMachine = new EnigmaMachine(new char[]{'M', 'O', 'D'});

		// these are sample encryptions for other implementations, trusting that they're correct
		assertEquals("JCQMIGLINIHRPOPSMWBXTWZCZJ", enigmaMachine.encrypt("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		enigmaMachine.reset();
		assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", enigmaMachine.decrypt("JCQMIGLINIHRPOPSMWBXTWZCZJ"));

		enigmaMachine.reset();
		assertEquals("LTGSDUIQET", enigmaMachine.encrypt("HELLOWORLD"));
		enigmaMachine.reset();
		assertEquals("HELLOWORLD", enigmaMachine.encrypt("LTGSDUIQET"));
	}

	@Test
	public void testEncrypt() {

		List<SteckerCable> steckeredPairs = new ArrayList<SteckerCable>();
		steckeredPairs.add(new SteckerCable('D', 'E'));
		steckeredPairs.add(new SteckerCable('C', 'N'));
		EnigmaMachine enigmaMachine = new EnigmaMachine(new char[]{'M', 'O', 'D'}, steckeredPairs);

		// these are sample encryptions for other implementations, trusting that they're correct
		assertEquals("JNIQOGLICIHRPAPSMWBXTWZNZJ", enigmaMachine.encrypt("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
		enigmaMachine.reset();
		assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", enigmaMachine.decrypt("JNIQOGLICIHRPAPSMWBXTWZNZJ"));
	}

	@Test
	public void testEncryptInvalid() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			EnigmaMachine enigmaMachine = new EnigmaMachine();

			enigmaMachine.encrypt("ABC1");
		});
	}
}
