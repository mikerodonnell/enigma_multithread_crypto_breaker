package com.demo.crypto.enigma.model;

import java.util.Arrays;

import com.demo.crypto.enigma.model.util.Alphabet;

public class EnigmaMachine {

	private static final char[] DEFAULT_INITIAL_POSITIONS = {'A','A','A'};
	private final char[] initialPositions;
	
	private AbstractEnigmaRotor slowRotor;
	private AbstractEnigmaRotor middleRotor;
	private AbstractEnigmaRotor fastRotor;
	private AbstractEnigmaReflector reflector;
	
	public EnigmaMachine() {
		this(DEFAULT_INITIAL_POSITIONS);
	}
	
	public EnigmaMachine( char[] initialPositions ) { // TODO: validation, configurationexception
		this.initialPositions = initialPositions;
		
		slowRotor = new EnigmaIRotor1( initialPositions[0], null );
		middleRotor = new EnigmaIRotor2( initialPositions[1], slowRotor );
		fastRotor = new EnigmaIRotor3( initialPositions[2], middleRotor );
		
		reflector = new WideBReflector();
	}
	
	public void set( char[] initialPositions ) {
		fastRotor.setOffset( Alphabet.indexOf(initialPositions[2]) );
		middleRotor.setOffset( Alphabet.indexOf(initialPositions[1]) );
		slowRotor.setOffset( Alphabet.indexOf(initialPositions[0]) );
	}
	
	public void reset() {
		fastRotor.setOffset( Alphabet.indexOf(initialPositions[2]) );
		middleRotor.setOffset( Alphabet.indexOf(initialPositions[1]) );
		slowRotor.setOffset( Alphabet.indexOf(initialPositions[0]) );
	}
	
	public String decrypt( final String cipherText ) { // just an alias #encrypt()
		return encrypt(cipherText);
	}
	
	public String encrypt( final String plainText ) {
		StringBuilder stringBuilder = new StringBuilder();
		for( int index=0; index<plainText.length(); index++ )
			stringBuilder.append( encrypt( plainText.charAt(index) ) );
		
		return stringBuilder.toString();
	}
	
	public char encrypt( char input ) {
		fastRotor.step();
		
		int originAbsoluteIndex = Alphabet.indexOf(input);
		int indexEnteringReflector = slowRotor.getOutputIndex(middleRotor.getOutputIndex(fastRotor.getOutputIndex(originAbsoluteIndex)));
		
		int indexLeavingReflector = reflector.getOutputIndex(indexEnteringReflector);
		
		int indexToLightBoard = fastRotor.getOutputIndexInverse(middleRotor.getOutputIndexInverse(slowRotor.getOutputIndexInverse(indexLeavingReflector)));
		return Alphabet.ALPHABET_ARRAY[indexToLightBoard];
	}
	
	public char decrypt( char cipherCharacter ) { // just an alias for #encrypt()
		return encrypt(cipherCharacter);
	}
}
