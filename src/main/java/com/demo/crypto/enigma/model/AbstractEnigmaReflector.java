package com.demo.crypto.enigma.model;

import org.apache.commons.lang3.ArrayUtils;

import com.demo.crypto.enigma.model.util.Alphabet;

public abstract class AbstractEnigmaReflector {

	protected final char[] substitutions;
	
	public AbstractEnigmaReflector( char[] substitutions ) {
		this.substitutions = substitutions;
	}
	
	
	public int getOutputIndex( int absoluteInputIndex ) {
		// the reflector doesn't move (step), there's no offset to worry about.
		//char substituteCharacter = getSubstitutions()[absoluteInputIndex];
		char substituteCharacter = substitutions[absoluteInputIndex];
		return Alphabet.indexOf(substituteCharacter);
	}
	
	public int getOutputIndexInverse( int absoluteInputIndex ) {
		char originalCharacter = Alphabet.ALPHABET_ARRAY[absoluteInputIndex];
		return ArrayUtils.indexOf(substitutions, originalCharacter);
	}
	
	protected char[] getSubstitutions() {
		return this.substitutions;
	}
}
