package com.demo.crypto.enigma.model.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SteckerPairTracker implements Iterator<Map<Character, Character>> {
	
	Map<Character, Character> currentPairs = null;
	
	
	@Override
	public boolean hasNext() {
		if( currentPairs==null || currentPairs.isEmpty() )
			return true;
		
		Character currentKey = currentPairs.keySet().iterator().next();
		if(currentKey.equals('Y')) {
			Character currentValue = currentPairs.get(currentKey);
			if(currentValue.equals('Z'));
				return false;
		}
		
		return true;
	}
	
	@Override
	public Map<Character, Character> next() {
		if( currentPairs == null ) {
			currentPairs = new HashMap<Character, Character>();
		}
		else if( currentPairs.isEmpty() ) {
			currentPairs.put('A', 'B');
		}
		else {
			Character currentKey = currentPairs.keySet().iterator().next();
			Character currentValue = currentPairs.get(currentKey);
			
			if(currentValue.equals('Z')) {
				int currentKeyIndex = Alphabet.indexOf(currentKey);
				currentPairs.clear();
				currentPairs.put( Alphabet.ALPHABET_ARRAY[currentKeyIndex+1], Alphabet.ALPHABET_ARRAY[currentKeyIndex+2] );
			}
			else {
				currentPairs.put( currentKey, Alphabet.nextCharacter(currentValue) );
			}
		}
		
		return currentPairs;
	}

	@Override
	public void remove() {
		// nothing to do
	}
}
