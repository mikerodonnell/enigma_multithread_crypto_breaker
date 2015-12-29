package com.demo.crypto.enigma.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AlphabetTest {

	@Test
	public void testNextCharacter() {
		assertEquals( 'B', Alphabet.nextCharacter('A') );
		assertEquals( 'Z', Alphabet.nextCharacter('Y') );
		assertEquals( 'A', Alphabet.nextCharacter('Z') );
	}
	
}
