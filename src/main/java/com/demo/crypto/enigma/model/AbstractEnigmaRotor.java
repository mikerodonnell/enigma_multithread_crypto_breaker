package com.demo.crypto.enigma.model;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import com.demo.crypto.enigma.util.Alphabet;

public abstract class AbstractEnigmaRotor implements Steppable {
	
	protected final char[] substitutions;
	protected final char turnoverCharacter;
	protected int offset; // offset of 0 = A position, offset of 1 = B position, offset of 25 = Z position.
	protected AbstractEnigmaRotor leftRotor;
	
	public AbstractEnigmaRotor( char[] substitutions, char turnoverCharacter ) {
		this('A', substitutions, turnoverCharacter, null);
	}
	
	public AbstractEnigmaRotor( char initialPosition, char[] substitutions, char turnoverCharacter, AbstractEnigmaRotor leftRotor ) {
		this.substitutions = substitutions;
		this.turnoverCharacter = turnoverCharacter;
		this.offset = Alphabet.indexOf(initialPosition);
		this.leftRotor = leftRotor;
	}
	
	
	@Override
	public int getOffset() {
		return offset;
	}
	
	@Override
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	public AbstractEnigmaRotor getLeftRotor() {
		return leftRotor;
	}
	
	@Override
	public char getTurnoverCharacter() {
		return turnoverCharacter;
	}
	
	public int getOutputIndex( int absoluteInputIndex ) {
		// the value of absoluteInputIndex assumes a static 'A' rotor position where 0 is at the top. if the rotor has any offset currently, 
		// we have to add the offset.
		int effectiveInputIndex = ((absoluteInputIndex + getOffset()) % 26); // we have to mod 26 here because the added offset might have put us over 25
		char substituteCharacter = substitutions[effectiveInputIndex];
		return ((26 + Alphabet.indexOf(substituteCharacter) - getOffset()) % 26 ); // now we have to add 26 and mod 26 again because the subtracted offset might have put us under 0
	}
	
	public int getOutputIndexInverse( int absoluteInputIndex ) {
		int effectiveInputIndex = (absoluteInputIndex + getOffset()) % 26; // we have to mod 26 here because the added offset might have put us over 25
		char originalCharacter = Alphabet.ALPHABET_ARRAY[effectiveInputIndex];
		return ( (26 + ArrayUtils.indexOf(substitutions, originalCharacter) - getOffset()) % 26 ); // now we have to add 26 and mod 26 again because the subtracted offset might have put us under 0
	}
	
	public void step() {
		offset++;
		
		if( leftRotor!=null && (offset == (Alphabet.indexOf(turnoverCharacter)+1)) )
			leftRotor.step();
		
		if( offset > 25)
			offset = 0;
	}
	
	protected char[] getSubstitutions() {
		return this.substitutions;
	}
	
}
