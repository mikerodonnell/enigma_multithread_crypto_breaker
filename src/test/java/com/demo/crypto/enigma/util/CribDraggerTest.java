package com.demo.crypto.enigma.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.demo.crypto.enigma.exception.NoMatchingCribException;
import com.demo.crypto.enigma.model.crib.Crib;
import com.demo.crypto.enigma.model.crib.NoSpecialOccurrences;
import com.demo.crypto.enigma.model.crib.ToCommandingAdmiralOfUBoats;

public class CribDraggerTest {

	private CribDragger cribDragger;
	
	@Before
	public void setup() {
		cribDragger = new CribDragger();
	}
	
	@Test( expected=NoMatchingCribException.class )
	public void testGetCribForMessageNoMatch() throws NoMatchingCribException {
		try{
			cribDragger.getCribForMessage("AKKKKKZZZZZZZZZZZZZZZZZZEZZ");
		}
		catch(NoMatchingCribException noMatchingCribException) { // verify both calls to #getCribForMessage() throw a NoMatchingCribException
			cribDragger.getCribForMessage("AZZZZZZZZZZ");
		}
	}
	
	@Test
	public void testGetCribForMessage() throws NoMatchingCribException {
		Crib crib = cribDragger.getCribForMessage("ZZZZZZZZZZZZZZZZZZZ");
		assertTrue( crib instanceof ToCommandingAdmiralOfUBoats );
		assertEquals( new Integer(0), crib.getStartIndex() );
		
		crib = cribDragger.getCribForMessage("YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY");
		assertTrue( crib instanceof NoSpecialOccurrences );
		assertEquals( new Integer(0), crib.getStartIndex() );
	}
	
	@Test( expected=NoMatchingCribException.class )
	public void testGetCribForMessageTooShort() throws NoMatchingCribException {
		cribDragger.getCribForMessage("ZZ");
	}
	
	@Test( expected=NoMatchingCribException.class )
	public void testGetCribForMessageEmpty() throws NoMatchingCribException {
		try {
			cribDragger.getCribForMessage(new char[0]);
		}
		catch(NoMatchingCribException noMatchingCribException) { // verify both calls to #getCribForMessage() throw a NoMatchingCribException
			cribDragger.getCribForMessage("");
		}
	}
	
	@Test
	public void testGetCribForMessageMultipleMatches() throws NoMatchingCribException {
		// verify that we get the longest known crib here. multiple cribs match, but KEINEBESONDERENEREIGNISSEXZUXBERICHTENXHEUTE is the longest one.
		Crib crib = cribDragger.getCribForMessage("YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY");
		assertTrue( crib instanceof NoSpecialOccurrences );
		assertEquals( new Integer(0), crib.getStartIndex() );
	}
	
	
	// @Test  TODO: re-institute this test when we have a crib that has no defined startIndex
	public void testGetCribStartIndex() throws NoMatchingCribException {
		/* verify that the correct start index is returned -- specifically the first index where the crib could exist. for example:
			cipher text: AEEEEYYYYYYYYYYYYYYYYYYYYYYYY...
						 ANXGENERAL                        NO! (ANXGENERAL is only applicable for startIndex 0, where it can't exist because A can't encipher to A, so ANXGENERAL is out)
						 KEINEBESONDERENEREIGNISSE         NO! (KEINEBESONDERENEREIGNISSE can't exist at startIndex 0 because E can't encipher to E at index 1, so KEINEBESONDERENEREIGNISSE might work but not at this index)
						  KEINEBESONDERENEREIGNISSE        NO! (KEINEBESONDERENEREIGNISSE can't exist at startIndex 1 because E can't encipher to E at index 2, so KEINEBESONDERENEREIGNISSE might work but not at this index)
						   KEINEBESONDERENEREIGNISSE       NO! (KEINEBESONDERENEREIGNISSE can't exist at startIndex 2 because E can't encipher to E at index 3, so KEINEBESONDERENEREIGNISSE might work but not at this index)
						    KEINEBESONDERENEREIGNISSE      NO! (KEINEBESONDERENEREIGNISSE can't exist at startIndex 3 because E can't encipher to E at index 4, so KEINEBESONDERENEREIGNISSE might work but not at this index)
						     KEINEBESONDERENEREIGNISSE     YES! (KEINEBESONDERENEREIGNISSE can exist at startIndex 4, nothing in EZZZZZZZZZZZZZZZZZZZZZZZZZ would have to encipher to itself to get KEINEBESONDERENEREIGNISSE.
		*/
		Crib crib = cribDragger.getCribForMessage("AEEEEYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY");
		assertTrue( crib instanceof NoSpecialOccurrences );
		assertEquals( new Integer(4), crib.getStartIndex() );
	}
	
}
