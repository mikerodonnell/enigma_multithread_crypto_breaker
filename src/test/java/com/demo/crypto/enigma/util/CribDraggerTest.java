package com.demo.crypto.enigma.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.demo.crypto.enigma.exception.NoMatchingCribException;
import com.demo.crypto.enigma.model.crib.NoSpecialOccurrences;
import com.demo.crypto.enigma.model.crib.ToGeneral;

public class CribDraggerTest {

	// TODO: figure out priority, what if 2 cribs match? probably use the longer one
	
	
	@Test( expected=NoMatchingCribException.class )
	public void testGetCribForMessageNoMatch() throws NoMatchingCribException {
		try{
			CribDragger.getCribForMessage("AKKKKKZZZZZZZZZZZZZZZZZZEZZ");
		}
		catch(NoMatchingCribException noMatchingCribException) { // verify both calls to #getCribForMessage() throw a NoMatchingCribException
			CribDragger.getCribForMessage("AZZZZZZZZZZ");
		}
	}
	
	@Test
	public void testGetCribForMessage() throws NoMatchingCribException {
		assertTrue( CribDragger.getCribForMessage("ZZZZZZZZZZ") instanceof ToGeneral );
		
		assertTrue( CribDragger.getCribForMessage("AZZZZZZZZZZZZZZZZZZZZZZZZZ") instanceof NoSpecialOccurrences );
	}
	
	@Test( expected=NoMatchingCribException.class )
	public void testGetCribForMessageTooShort() throws NoMatchingCribException {
		CribDragger.getCribForMessage("ZZ");
	}
	
	@Test( expected=NoMatchingCribException.class )
	public void testGetCribForMessageEmpty() throws NoMatchingCribException {
		try {
			CribDragger.getCribForMessage(new char[0]);
		}
		catch(NoMatchingCribException noMatchingCribException) { // verify both calls to #getCribForMessage() throw a NoMatchingCribException
			CribDragger.getCribForMessage("");
		}
	}
	
	@Test
	public void testGetCribForMessageMultipleMatches() throws NoMatchingCribException {
		// verify that we get the longest known crib here
		assertTrue( CribDragger.getCribForMessage("ZZZZZZZZZZZZZZZZZZZZZZZZZ") instanceof NoSpecialOccurrences );
	}
}
