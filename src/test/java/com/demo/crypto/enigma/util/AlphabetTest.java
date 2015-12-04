package com.demo.crypto.enigma.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.demo.crypto.enigma.model.util.Alphabet;

public class AlphabetTest {

	@Test
	public void testNextCharacter() {
		assertEquals( 'B', Alphabet.nextCharacter('A') );
		assertEquals( 'Z', Alphabet.nextCharacter('Y') );
		assertEquals( 'A', Alphabet.nextCharacter('Z') );
	}
	
}
