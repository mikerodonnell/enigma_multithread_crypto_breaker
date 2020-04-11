package com.demo.crypto.enigma.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlphabetTest {

	@Test
	public void testIndexOf() {
		assertEquals(0, Alphabet.indexOf('A'));
		assertEquals(0, Alphabet.indexOf('a'));
		assertEquals(3, Alphabet.indexOf('D'));
		assertEquals(3, Alphabet.indexOf('d'));
		assertEquals(25, Alphabet.indexOf('Z'));
		assertEquals(25, Alphabet.indexOf('z'));

		assertEquals(null, Alphabet.indexOf('~'));
	}

	@Test
	public void testNext() {
		assertEquals('B', Alphabet.next('A'));
		assertEquals('B', Alphabet.next('a'));
		assertEquals('Z', Alphabet.next('Y'));
		assertEquals('Z', Alphabet.next('y'));
		assertEquals('A', Alphabet.next('Z'));
		assertEquals('A', Alphabet.next('z'));

		assertEquals(null, Alphabet.next('~'));
	}

	@Test
	public void testAfterNext() {
		assertEquals('C', Alphabet.afterNext('A'));
		assertEquals('C', Alphabet.afterNext('a'));
		assertEquals('A', Alphabet.afterNext('Y'));
		assertEquals('A', Alphabet.afterNext('y'));
		assertEquals('B', Alphabet.afterNext('Z'));
		assertEquals('B', Alphabet.afterNext('z'));

		assertEquals(null, Alphabet.next('~'));
	}
}
