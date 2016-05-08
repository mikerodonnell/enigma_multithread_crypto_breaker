package com.demo.crypto.enigma.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AlphabetTest {

	@Test
	public void testIndexOf() {
		assertEquals( new Integer(0), Alphabet.indexOf('A') );
		assertEquals( new Integer(0), Alphabet.indexOf('a') );
		assertEquals( new Integer(3), Alphabet.indexOf('D') );
		assertEquals( new Integer(3), Alphabet.indexOf('d') );
		assertEquals( new Integer(25), Alphabet.indexOf('Z') );
		assertEquals( new Integer(25), Alphabet.indexOf('z') );
		
		assertEquals( null, Alphabet.indexOf('~') );
	}
	
	@Test
	public void testNext() {
		assertEquals( new Character('B'), Alphabet.next('A') );
		assertEquals( new Character('B'), Alphabet.next('a') );
		assertEquals( new Character('Z'), Alphabet.next('Y') );
		assertEquals( new Character('Z'), Alphabet.next('y') );
		assertEquals( new Character('A'), Alphabet.next('Z') );
		assertEquals( new Character('A'), Alphabet.next('z') );
		
		assertEquals( null, Alphabet.next('~') );
	}
	
	@Test
	public void testAfterNext() {
		assertEquals( new Character('C'), Alphabet.afterNext('A') );
		assertEquals( new Character('C'), Alphabet.afterNext('a') );
		assertEquals( new Character('A'), Alphabet.afterNext('Y') );
		assertEquals( new Character('A'), Alphabet.afterNext('y') );
		assertEquals( new Character('B'), Alphabet.afterNext('Z') );
		assertEquals( new Character('B'), Alphabet.afterNext('z') );
		
		assertEquals( null, Alphabet.next('~') );
	}
}
