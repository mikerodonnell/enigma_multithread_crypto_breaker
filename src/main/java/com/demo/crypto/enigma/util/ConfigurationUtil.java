package com.demo.crypto.enigma.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import com.demo.crypto.enigma.model.SteckerCable;

public class ConfigurationUtil {
	
	private static Random random = new Random();

	
	public static char[] generateRandomRotorPositions() {
		char[] rotorPositions = new char[3];
		
		rotorPositions[0] = Alphabet.ALPHABET_ARRAY[ random.nextInt(25) ];
		rotorPositions[1] = Alphabet.ALPHABET_ARRAY[ random.nextInt(25) ];
		rotorPositions[2] = Alphabet.ALPHABET_ARRAY[ random.nextInt(25) ];
		
		return rotorPositions;
	}
	
	public static char[] getPositionsFromString( final String input ) { // TODO: validation
		if( StringUtils.isNotBlank(input) )
			return input.trim().toUpperCase().toCharArray();
			
		return new char[3];
	}
	
	public static List<SteckerCable> generateRandomSteckers( int steckerCount ) {
		List<SteckerCable> steckeredPairs = new ArrayList<SteckerCable>();
		
		// Arrays#asList() returns an unmodifiable List, we have to construct our own, might as well make a LinkedList.
		List<Character> unusedCharacters = new LinkedList<Character>(Arrays.asList( Alphabet.ALPHABET_ARRAY ));
		for( int count=0; count<steckerCount; count++ ) {
			int inputIndex = random.nextInt( unusedCharacters.size() );
			Character input = unusedCharacters.get(inputIndex);
			unusedCharacters.remove(inputIndex); // now remove this character from unusedCharacters so we don't re-use it
			
			int outputIndex = random.nextInt( unusedCharacters.size() );
			Character output = unusedCharacters.get(outputIndex);
			unusedCharacters.remove(outputIndex);
			
			steckeredPairs.add( new SteckerCable(input, output) );
		}
		
		return steckeredPairs;
	}
	
}
