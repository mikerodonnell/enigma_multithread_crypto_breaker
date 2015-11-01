package com.demo.crypto.enigma.model;


// Enigma I Rotor 2, 1930.  AJDKSIRUXBLHWTMCQGZNPYFVOE	1930
public class EnigmaIRotor2 extends AbstractEnigmaRotor {

	private static final char[] SUBSTITUTIONS = {'A', 'J', 'D', 'K', 'S', 'I', 'R', 'U', 'X', 'B', 'L', 'H', 'W', 'T', 'M', 'C', 'Q', 'G', 'Z', 'N', 'P', 'Y', 'F', 'V', 'O', 'E'};
	private static final char TURNOVER_CHARACTER = 'E'; // if rotor steps from E to F, the next rotor is advanced
	
	
	public EnigmaIRotor2( AbstractEnigmaRotor leftRotor ) {
		this( 'A', leftRotor);
	}
	
	public EnigmaIRotor2( char initialPosition, AbstractEnigmaRotor leftRotor ) {
		// TODO: validations! leftRotor must be some different kind of rotor!
		super(initialPosition, SUBSTITUTIONS, TURNOVER_CHARACTER, leftRotor);
	}
	
	
	@Override
	public String toString() {
		return "Enigma I Rotor II, 1930";
	}
}
