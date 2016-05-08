package com.demo.crypto.enigma.util;

import java.util.HashMap;
import java.util.Map;

/**
 * utilities for working with the English alphabet.
 * 
 * @author Mike O'Donnell  github.com/mikerodonnell
 */
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
	
	/**
	 * get the index of the given character in the English alphabet, 0..25. case insensitive.
	 * 
	 * @param character
	 * @return the integer in range 0..25 corresponding the given character if a valid English alphabet letter; null otherwise;
	 */
	public static Integer indexOf(char character) {
		return ALPHABET.get(Character.toUpperCase(character));
	}
	
	/**
	 * get the next letter in the English alphabet, in uppercase. returns A for Z.
	 * 
	 * @param character
	 * @return the next letter of the alphabet if a valid English alphabet character is given (A returned for Z); null otherwise.
	 */
	public static Character next(char character) {
		Integer indexOfGiven = indexOf(character);
		
		if(indexOfGiven==null)
			return null;
		
		return ALPHABET_ARRAY[ (indexOfGiven+1) % 26 ];
	}
	
	/**
	 * the English alphabet character two letters after the given letter. returns A for Y and B for Z.
	 * 
	 * @param character
	 * @return the next-next letter of the alphabet if a valid English alphabet character is given (A returned for Y, B for Z); null otherwise.
	 */
	public static Character afterNext(char character) {
		Integer indexOfGiven = indexOf(character);
		
		if(indexOfGiven==null)
			return null;
		
		return ALPHABET_ARRAY[ (indexOfGiven+2) % 26 ];
	}
}
