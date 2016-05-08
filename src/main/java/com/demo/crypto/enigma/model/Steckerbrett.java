package com.demo.crypto.enigma.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.demo.crypto.enigma.exception.DuplicateSteckerException;
import com.demo.crypto.enigma.exception.InvalidConfigurationException;

/**
 * representation of the Enigma machine plug board, or Steckerbrett. a number of wires, each plugged into two letters, swaps output between the two. for example,
 * plugging a wire, or stecker, into C and R results in output that would otherwise be C now being R, and vice versa.
 * 
 * @author Mike O'Donnell  github.com/mikerodonnell
 */
public class Steckerbrett {
	
	/** the mapping of substitution pair letters representing the Steckerbrett's wiring state. this is maintained reflexively -- if A=>B is added then 
	 * B=>A should also be added.
	 */
	private Map<Character, Character> steckeredPairs = new HashMap<Character, Character>();

	Steckerbrett() { }
	
	Steckerbrett( Collection<SteckerCable> steckeredPairs ) {
		stecker(steckeredPairs);
	}
	
	public void stecker( final Collection<SteckerCable> steckeredPairs ) {
		if( steckeredPairs != null && !steckeredPairs.isEmpty() ) {
			if( steckeredPairs.size() > 10 )
				throw new InvalidConfigurationException("a maximum of 10 steckered pairs is supported.");
			
			// we can't just copy steckeredPairs into this.steckeredPairs; have to validate each one.
			for( SteckerCable steckerCable : steckeredPairs )
				stecker( steckerCable.getInput(), steckerCable.getOutput() );
		}
	}
	
	public void stecker( char original, char alternate) {
		if( steckeredPairs.keySet().contains(original) || steckeredPairs.values().contains(original) )
			throw new DuplicateSteckerException(original + " is already steckered. clear steckered pairs to change steckering of " + original);
		
		if( steckeredPairs.keySet().contains(alternate) || steckeredPairs.values().contains(alternate) )
			throw new DuplicateSteckerException(alternate + " is already steckered. clear steckered pairs to change steckering of " + alternate);
		
		steckeredPairs.put( original, alternate ); // reflexive. if A=>B then B=>A.
		steckeredPairs.put( alternate, original );
	}
	
	public void clear() {
		steckeredPairs.clear();
	}
	
	public char getSteckeredCharacter( char original ) {
		char retval = original;
		
		if( steckeredPairs.containsKey(original) )
			retval = steckeredPairs.get(original);
		// we don't have to check steckeredPairs.values().contains(original) because steckeredPairs is maintained reflexively.
		
		return retval;
	}
	
}
