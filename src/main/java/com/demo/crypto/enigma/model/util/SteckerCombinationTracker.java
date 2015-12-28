package com.demo.crypto.enigma.model.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.demo.crypto.enigma.model.SteckerCable;
import com.demo.crypto.enigma.model.exception.InvalidConfigurationException;

public class SteckerCombinationTracker implements Iterator<List<SteckerCable>> {
	
	public static final int DEFAULT_STECKER_PAIR_COUNT = 3; // TODO: 10
	private int steckerPairCount = DEFAULT_STECKER_PAIR_COUNT;
	List<SteckerCable> currentPairs = null;
	
	/** the number of combinations that have been given so far (the number of times #next() has been called). may be used for tracking progress. */
	private long combinationsCount = 0;
	
	
	public SteckerCombinationTracker() { }
	
	public SteckerCombinationTracker(int steckerPairCount) {
		if( steckerPairCount < 0 )
			throw new InvalidConfigurationException("stecker pair count can't be negative.");
		
		if( steckerPairCount > 10 )
			throw new InvalidConfigurationException("a maximum of 10 steckered pairs is supported.");
		
		this.steckerPairCount = steckerPairCount;
	}
	
	@Override
	public boolean hasNext() {
		if( currentPairs != null && currentPairs.size()==2 ) { // TODO: this should be size 10
			if( currentPairs.get(0).getInput()=='Y' && currentPairs.get(0).getOutput()=='Z' ) {
				return( currentPairs.get(1).getInput()=='W' && currentPairs.get(1).getOutput()=='X' );
			}
		}
		
		return true;
	}
	
	@Override
	public List<SteckerCable> next() {
		if( currentPairs == null ) {
			currentPairs = new ArrayList<SteckerCable>();
		}
		else if( currentPairs.isEmpty() ) {
			
			if( steckerPairCount >=1 )
				currentPairs.add( new SteckerCable('A', 'B') );
			
			if( steckerPairCount >=2 )
				currentPairs.add( new SteckerCable('C', 'D') );
			
			if( steckerPairCount >=3 )
				currentPairs.add( new SteckerCable('E', 'F') );
			
			if( steckerPairCount >=4 )
				currentPairs.add( new SteckerCable('G', 'H') );
		}
		else {
			for(int index=currentPairs.size()-1; index>=0; index--) {
				if( incrementCableAtIndex(index) || index==0 )
					break;
				
				char nextRightInput = Alphabet.nextCharacter(currentPairs.get(index-1).getInput());
				char nextRightOutput = Alphabet.nextCharacter(nextRightInput);
				currentPairs.set( index, new SteckerCable(nextRightInput, nextRightOutput) );
			}
		}
		
		combinationsCount++;
		return currentPairs;
	}

	private boolean incrementCableAtIndex( int index ) {
		SteckerCable steckerCable = currentPairs.get(index);
		
		if(steckerCable.getOutput() =='Z') {
			
			if( steckerCable.getInput() == 'Y' )
				return false;
			
			steckerCable.setInput( Alphabet.nextCharacter(steckerCable.getInput()) );
			steckerCable.setOutput( Alphabet.nextCharacter(steckerCable.getInput()) );
		}
		else {
			steckerCable.setOutput( Alphabet.nextCharacter(steckerCable.getOutput()) );
		}
		
		return true;
	}
	
	@Override
	public void remove() {
		// nothing to do
	}

	public long getCombinationsCount() {
		return combinationsCount;
	}
	// no setter!
}
