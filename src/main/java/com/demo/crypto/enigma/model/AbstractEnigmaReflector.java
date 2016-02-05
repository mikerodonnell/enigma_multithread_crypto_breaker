package com.demo.crypto.enigma.model;

import com.demo.crypto.enigma.util.Alphabet;

public abstract class AbstractEnigmaReflector {

	/** the output characters for this reflector. for example, substitutions => [K, R, N, A ...] means this relector outputs K for input A, R for input B, N for input C, etc. */
	protected final char[] substitutions;
	
	public AbstractEnigmaReflector( char[] substitutions ) {
		this.substitutions = substitutions;
	}
	
	/**
	 * get the index (0 through 25) of the letter of the alphabet that this reflector outputs for the given input index. for example, if the reflector outputs E for input
	 * B, then #getOutputIndex(1) returns 4.
	 * 
	 * @param absoluteInputIndex
	 * @return
	 */
	public int getOutputIndex( int absoluteInputIndex ) {
		// the reflector doesn't move (step), there's no offset to worry about.
		char substituteCharacter = substitutions[absoluteInputIndex];
		return Alphabet.indexOf(substituteCharacter);
	}
	
}
