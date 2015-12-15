package com.demo.crypto.enigma.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.demo.crypto.enigma.model.exception.InvalidConfigurationException;

public class Steckerbrett {
	
	private Map<Character, Character> steckeredPairs = new HashMap<Character, Character>();

	Steckerbrett() { }
	
	Steckerbrett( List<SteckerCable> steckeredPairs ) {
		stecker(steckeredPairs);
	}
	
	public void stecker( final List<SteckerCable> steckeredPairs ) {
		if( steckeredPairs != null && !steckeredPairs.isEmpty() ) {
			if( !(steckeredPairs.size() == 2) ) // TODO: this should be 10
				throw new InvalidConfigurationException("only 0 or exactly 10 steckered pairs are supported.");
			
			// we can't just copy steckeredPairs into this.steckeredPairs; have to validate each one.
			for( SteckerCable steckerCable : steckeredPairs )
				stecker( steckerCable.getInput(), steckerCable.getOutput() );
		}
	}
	
	public void stecker( char original, char alternate) {
		if( steckeredPairs.keySet().contains(original) || steckeredPairs.values().contains(original) )
			throw new InvalidConfigurationException(original + " is already steckered. clear steckered pairs to change steckering of " + original);
		
		if( steckeredPairs.keySet().contains(alternate) || steckeredPairs.values().contains(alternate) )
			throw new InvalidConfigurationException(alternate + " is already steckered. clear steckered pairs to change steckering of " + alternate);
		
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
		
		return retval;
	}
	
}
