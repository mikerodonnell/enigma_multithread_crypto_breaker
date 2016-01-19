package com.demo.crypto.enigma.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AlphabetTest {

	@Test
	public void testNextCharacter() {
		assertEquals( 'B', Alphabet.next('A') );
		assertEquals( 'Z', Alphabet.next('Y') );
		assertEquals( 'A', Alphabet.next('Z') );
	}
	
}
