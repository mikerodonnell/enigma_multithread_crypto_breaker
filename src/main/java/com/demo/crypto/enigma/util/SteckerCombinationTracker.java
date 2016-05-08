package com.demo.crypto.enigma.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.demo.crypto.enigma.model.SteckerCable;
import com.demo.crypto.enigma.exception.InvalidConfigurationException;

/**
 * helper class for iterating over all possible steckerbrett (plug board) combinations.
 * 
 * @author Mike O'Donnell  github.com/mikerodonnell
 */
public class SteckerCombinationTracker implements Iterator<List<SteckerCable>> {
	
	public static final int DEFAULT_STECKER_PAIR_COUNT = 3; // TODO: 10
	private int steckerPairCount = DEFAULT_STECKER_PAIR_COUNT;
	List<SteckerCable> currentPairs = null;
	
	/** the number of combinations that have been given so far (the number of times #next() has been called). may be used for tracking progress. */
	private long combinationsCount = 0;
	
	
	public SteckerCombinationTracker() {
		this(DEFAULT_STECKER_PAIR_COUNT);
	}
	
	public SteckerCombinationTracker(int steckerPairCount) {
		if( steckerPairCount < 0 )
			throw new InvalidConfigurationException("stecker pair count can't be negative.");
		
		if( steckerPairCount > 10 )
			throw new InvalidConfigurationException("a maximum of 10 steckered pairs is supported.");
		
		this.steckerPairCount = steckerPairCount;
	}
	
	@Override
	public boolean hasNext() {
		
		boolean hasNext = true;
		// every instance of SteckerCombinationTracker returns an empty List first, regardless of steckerPairCount. so, there's a next pair unless:
		//  * steckerPairCount is 0, AND we've already given out our empty List
		//      - OR -
		//  * the rightmost cable is at Y<=>Z, the one to its left is at W<=>X, the one to that cable's left is at U<=>V, etc.
		
		if( combinationsCount > 0 ) { // if next() has never been called, hasNext is true.
			if( steckerPairCount==0 )
				hasNext = false;
			
			else if( currentPairs.isEmpty() ) // if currentPairs is empty, next() has been called exactly once, we have more pairs as long as steckerPairCount>0
				hasNext = true;
			
			else {
				boolean hasCableThatCanMove = false;
				
				// index=0 is the leftmost cable. start at the rightmost and work our way left, seeing if any of our stecker cables can move to the right. if all our
				// cables are as far right as they can go (for example, if 2 cables are in use and they're at positions W<=>X and Y<=>Z), then we're out of combinations.
				for( int index=steckerPairCount-1; index>=0; index-- ) {
					SteckerCable cable = currentPairs.get(index);
					
					// lastInputForThisCable tracks how far right we need to move a cable ... not all the way to YZ unless we only have 1 total cable.
					// if index=2 and steckerPairCount=3, lastInputForThisCable = Y (index 24)
					// if index=1 and steckerPairCount=3, lastInputForThisCable = W (index 22)
					// if index=0 and steckerPairCount=3, lastInputForThisCable = U (index 20)
					char lastInputForThisCable = Alphabet.ALPHABET_ARRAY[25-2*(steckerPairCount-1-index)-1];
					char lastOutForThisCable = Alphabet.next(lastInputForThisCable);
					
					if( cable.getInput() != lastInputForThisCable || cable.getOutput() != lastOutForThisCable ) {
						hasCableThatCanMove = true;
						break; // this cable is NOT all the way right yet, therefore hasNext() is true!
					}
				}
				
				if( !hasCableThatCanMove )
					hasNext = false;
			}
		}
		
		return hasNext;
	}
	
	
	@Override
	public List<SteckerCable> next() {
		if( currentPairs == null ) {
			currentPairs = new ArrayList<SteckerCable>();
		}
		else if( currentPairs.isEmpty() ) {
			if( steckerPairCount >=1 ) {
				currentPairs.add( new SteckerCable('A', 'B') );
			
				// now, plug the rest of the stecker cables into their starting positions. though the leftmost can can be anywhere depending on the parallelThreadIndex, the 
				// rest of the cables always start immediately to the right.
				for( int index=0; index<steckerPairCount-1; index++ )
					currentPairs.add( new SteckerCable(Alphabet.next(currentPairs.get(index).getOutput()), Alphabet.afterNext(currentPairs.get(index).getOutput())) );
			}
		}
		else {
			for(int index=currentPairs.size()-1; index>=0; index--) {
				boolean isLeftCableInputChanging = ( index>0 && ('Z' == currentPairs.get(index-1).getOutput()) );
				
				if( incrementCableAtIndex(index) || index==0 )
					break;
				
				char nextRightInput = Alphabet.next(currentPairs.get(index-1).getInput());
				
				if(isLeftCableInputChanging)
					nextRightInput = Alphabet.next(Alphabet.next(nextRightInput));
				
				char nextRightOutput = Alphabet.next(nextRightInput);
				
				currentPairs.set( index, new SteckerCable(nextRightInput, nextRightOutput) );
			}
		}
		
		combinationsCount++;
		return currentPairs;
	}

	private boolean incrementCableAtIndex( int index ) {
		SteckerCable steckerCable = currentPairs.get(index);
		
		if(steckerCable.getOutput() == 'Z') {
			
			if( steckerCable.getInput() == 'Y' )
				return false;
			
			steckerCable.setInput( Alphabet.next(steckerCable.getInput()) );
			steckerCable.setOutput( Alphabet.next(steckerCable.getInput()) );
		}
		else {
			steckerCable.setOutput( Alphabet.next(steckerCable.getOutput()) );
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
