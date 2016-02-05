package com.demo.crypto.enigma.util;

import java.util.HashMap;
import java.util.Map;

public class Alphabet {

	public static final Character[] ALPHABET_ARRAY = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	
	/** A static Map of alphabet letters to their indices. used for improved performance over ArrayUtils.indexOf(ALPHABET_ARRAY) for frequent lookups. */
	private static final Map<Character, Integer> ALPHABET = new HashMap<Character, Integer>();
	static {
		ALPHABET.put('A', 0);
		ALPHABET.put('B', 1);
		ALPHABET.put('C', 2);
		ALPHABET.put('D', 3);
		ALPHABET.put('E', 4);
		ALPHABET.put('F', 5);
		ALPHABET.put('G', 6);
		ALPHABET.put('H', 7);
		ALPHABET.put('I', 8);
		ALPHABET.put('J', 9);
		ALPHABET.put('K', 10);
		ALPHABET.put('L', 11);
		ALPHABET.put('M', 12);
		ALPHABET.put('N', 13);
		ALPHABET.put('O', 14);
		ALPHABET.put('P', 15);
		ALPHABET.put('Q', 16);
		ALPHABET.put('R', 17);
		ALPHABET.put('S', 18);
		ALPHABET.put('T', 19);
		ALPHABET.put('U', 20);
		ALPHABET.put('V', 21);
		ALPHABET.put('W', 22);
		ALPHABET.put('X', 23);
		ALPHABET.put('Y', 24);
		ALPHABET.put('Z', 25);
	}
	
	public static int indexOf(char character) {
		return ALPHABET.get(character);
	}
	
	/**
	 * the next letter in the alphabet. returns A for Z.
	 * 
	 * @param character
	 * @return
	 */
	public static char next(char character) {
		return ALPHABET_ARRAY[ (indexOf(character)+1) % 26 ];
	}
	
	/**
	 * the alphabet character two letters from the given letter. returns A for Y and B for Z.
	 * 
	 * @param character
	 * @return
	 */
	public static char afterNext(char character) {
		return ALPHABET_ARRAY[ (indexOf(character)+2) % 26 ];
	}
}
