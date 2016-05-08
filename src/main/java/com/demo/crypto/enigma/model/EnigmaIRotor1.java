package com.demo.crypto.enigma.model;

/**
 * representation of the Enigma I Rotor 1, 1930
 * 
 * @see <a href="https://en.wikipedia.org/wiki/Enigma_rotor_details">https://en.wikipedia.org/wiki/Enigma_rotor_details</a>
 * @author Mike O'Donnell  github.com/mikerodonnell
 */
public class EnigmaIRotor1 extends AbstractEnigmaRotor {

	private static final char[] SUBSTITUTIONS = {'E', 'K', 'M', 'F', 'L', 'G', 'D', 'Q', 'V', 'Z', 'N', 'T', 'O', 'W', 'Y', 'H', 'X', 'U', 'S', 'P', 'A', 'I', 'B', 'R', 'C', 'J'};
	private static final char TURNOVER_CHARACTER = 'Q'; // if rotor steps from Q to R, the next rotor is advanced
	
	
	public EnigmaIRotor1( AbstractEnigmaRotor leftRotor ) {
		this( 'A', leftRotor);
	}
	
	public EnigmaIRotor1( char initialPosition, AbstractEnigmaRotor leftRotor ) {
		// TODO: validations! leftRotor must be some different kind of rotor!
		super(initialPosition, SUBSTITUTIONS, TURNOVER_CHARACTER, leftRotor);
	}
	
	
	@Override
	public String toString() {
		return "Enigma I Rotor I, 1930";
	}
}
