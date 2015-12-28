package com.demo.crypto.enigma.util;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.demo.crypto.enigma.model.SteckerCable;
import com.demo.crypto.enigma.model.util.SteckerCombinationTracker;

public class SteckerPairTrackerTest {
	
	private SteckerCombinationTracker steckerCombinationTracker;
	
	@Before
	public void setup() {
		steckerCombinationTracker = new SteckerCombinationTracker();
	}
	
	
	@Test
	public void testHasNext() {
		for( int count=0; count<100; count++) {
			steckerCombinationTracker.next();
			assertTrue( steckerCombinationTracker.hasNext() );
		}
		
		assertTrue( steckerCombinationTracker.hasNext() );
	}
	
	@Test
	public void testNext() {
		assertEquals( 0, steckerCombinationTracker.next().size() );
		
		List<SteckerCable> testSteckerPairs = steckerCombinationTracker.next();
		/*
		assertEquals( 3, testSteckerPairs.size() );
		assertEquals( 'A', testSteckerPairs.get(0).getInput() );
		assertEquals( 'B', testSteckerPairs.get(0).getOutput() );
		assertEquals( 'C', testSteckerPairs.get(1).getInput() );
		assertEquals( 'D', testSteckerPairs.get(1).getOutput() );
		
		testSteckerPairs = steckerCombinationTracker.next();
		assertEquals( 3, testSteckerPairs.size() );
		assertEquals( 'A', testSteckerPairs.get(0).getInput() );
		assertEquals( 'B', testSteckerPairs.get(0).getOutput() );
		assertEquals( 'C', testSteckerPairs.get(1).getInput() );
		assertEquals( 'E', testSteckerPairs.get(1).getOutput() );
		
		for(int count=0; count<10000; count++) {
			System.out.println("~~~~~~~ test stecker pairs: " + testSteckerPairs);
			testSteckerPairs = steckerCombinationTracker.next();
		}
		*/
	}
	
}
