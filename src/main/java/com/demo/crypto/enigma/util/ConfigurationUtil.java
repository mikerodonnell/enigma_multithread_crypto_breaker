package com.demo.crypto.enigma.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import com.demo.crypto.enigma.model.SteckerCable;

/**
 * utilities for configuring the Enigma.
 * 
 * @author Mike O'Donnell  github.com/mikerodonnell
 */
public class ConfigurationUtil {
	
	private static Random random = new Random();

	
	/**
	 * generate a set of random rotor positions.
	 * 
	 * @return an array of 3 uppercase characters, with index 0 mapping to the the rightmost (fast) rotor. example: ['G', 'A', 'V']
	 */
	public static char[] generateRandomRotorPositions() {
		char[] rotorPositions = new char[3];
		
		rotorPositions[0] = Alphabet.ALPHABET_ARRAY[ random.nextInt(25) ];
		rotorPositions[1] = Alphabet.ALPHABET_ARRAY[ random.nextInt(25) ];
		rotorPositions[2] = Alphabet.ALPHABET_ARRAY[ random.nextInt(25) ];
		
		return rotorPositions;
	}
	
	/**
	 * convert the given 3-character String representation of starting rotor positions to a character array.
	 * 
	 * @param input example: "GAV"
	 * @return an array of 3 uppercase characters, with index 0 mapping to the the rightmost (fast) rotor. example: ['G', 'A', 'V']
	 */
	public static char[] getPositionsFromString( final String input ) {
		String trimmedInput = StringUtils.stripToNull(input);
		if( trimmedInput!=null && trimmedInput.length()==3 && StringUtils.isAlpha(trimmedInput) )
			return trimmedInput.toUpperCase().toCharArray();
			
		throw new IllegalArgumentException("Initial positions must be specified with 3 letters. ex: ARH");
	}
	
	/**
	 * @see {@link #generateRandomSteckers(int)}
	 */
	public static List<SteckerCable> generateRandomSteckers( final String input ) {
		int steckerCount = -1;
		try {
			steckerCount = Integer.valueOf(input);
		}
		catch(NumberFormatException numberFormatException) {
			throw new IllegalArgumentException("Must be a number between 0 and 10.");
		}
		
		if( steckerCount<0 || steckerCount>10 )
			throw new IllegalArgumentException("Must be a number between 0 and 10.");
		
		return generateRandomSteckers(steckerCount);
	}
	
	/**
	 * generate a set of random steckered pairs.
	 * 
	 * @param input the number of steckered pairs to generate. 1 to 10 inclusive.
	 * @return
	 */
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
	
	/**
	 * validate that the user choice of thread count is one of {1, 3, 4, 8}
	 * 
	 * @param input
	 * @return
	 */
	public static int validateThreadCount( final String input ) {
		int threadCount = 1;
		
		try {
			threadCount = Integer.valueOf(input);
		}
		catch(NumberFormatException numberFormatException) {
			throw new IllegalArgumentException("Must be 1, 2, 4, or 8");
		}
		
		if( threadCount!=1 && threadCount!=2 && threadCount!=4 && threadCount!=8 )
			throw new IllegalArgumentException("Unsupported number of threads. Must be 1, 2, 4, or 8");
		
		return threadCount;
	}
}
