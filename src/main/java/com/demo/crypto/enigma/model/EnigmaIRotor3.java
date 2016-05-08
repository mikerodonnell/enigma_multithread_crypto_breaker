package com.demo.crypto.enigma.model;

/**
 * representation of the Enigma I Rotor 3, 1930
 * 
 * @see <a href="https://en.wikipedia.org/wiki/Enigma_rotor_details">https://en.wikipedia.org/wiki/Enigma_rotor_details</a>
 * @author Mike O'Donnell  github.com/mikerodonnell
 */
public class EnigmaIRotor3 extends AbstractEnigmaRotor {

	private static final char[] SUBSTITUTIONS = {'B', 'D', 'F', 'H', 'J', 'L', 'C', 'P', 'R', 'T', 'X', 'V', 'Z', 'N', 'Y', 'E', 'I', 'W', 'G', 'A', 'K', 'M', 'U', 'S', 'Q', 'O'};
	private static final char TURNOVER_CHARACTER = 'V';
	
	
	public EnigmaIRotor3( AbstractEnigmaRotor leftRotor ) {
		this('A', leftRotor);
	}
	
	public EnigmaIRotor3( char initialPosition, AbstractEnigmaRotor leftRotor ) {
		// TODO: validations! leftRotor must be some different kind of rotor!
		super(initialPosition, SUBSTITUTIONS, TURNOVER_CHARACTER, leftRotor);
	}
	
	
	@Override
	public String toString() {
		return "Enigma I Rotor III, 1930";
	}
}
