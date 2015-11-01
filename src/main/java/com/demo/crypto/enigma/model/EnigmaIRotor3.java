package com.demo.crypto.enigma.model;


// Enigma I Rotor 1, 1930.  BDFHJLCPRTXVZNYEIWGAKMUSQO	1930
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
