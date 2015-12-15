package com.demo.crypto.enigma.model.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.demo.crypto.enigma.model.SteckerCable;

public class SteckerCombinationTracker implements Iterator<List<SteckerCable>> {
	
	List<SteckerCable> currentPairs = null;
	
	
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
			currentPairs.add( new SteckerCable('A', 'B') );
			currentPairs.add( new SteckerCable('C', 'D') );
		}
		else {
			if( incrementCableAtIndex(1) ) {
				// nothing else to do
			}
			else {
				incrementCableAtIndex(0);
				currentPairs.set( 1, new SteckerCable( Alphabet.nextCharacter(currentPairs.get(0).getInput()), Alphabet.nextCharacter(currentPairs.get(0).getOutput()) ) );
			}
		}
		
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
}
