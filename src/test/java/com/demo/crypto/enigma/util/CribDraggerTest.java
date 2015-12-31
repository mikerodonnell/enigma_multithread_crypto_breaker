package com.demo.crypto.enigma.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.demo.crypto.enigma.model.crib.NoSpecialOccurrences;
import com.demo.crypto.enigma.model.crib.ToGeneral;

public class CribDraggerTest {

	// TODO: figure out priority, what if 2 cribs match? probably use the longer one
	
	@Test
	public void testGetCribForMessageNoMatch() {
		assertNull( CribDragger.getCribForMessage("AKKKKKZZZZZZZZZZZZZZZZZZEZZ") );
		
		assertNull( CribDragger.getCribForMessage("AZZZZZZZZZZ") );
	}
	
	@Test
	public void testGetCribForMessage() {
		assertTrue( CribDragger.getCribForMessage("ZZZZZZZZZZ") instanceof ToGeneral );
		
		assertTrue( CribDragger.getCribForMessage("AZZZZZZZZZZZZZZZZZZZZZZZZZ") instanceof NoSpecialOccurrences );
	}
	
	@Test
	public void testGetCribForMessageTooShort() {
		assertNull( CribDragger.getCribForMessage("ZZ") );
	}
	
	@Test
	public void testGetCribForMessageEmpty() {
		assertNull( CribDragger.getCribForMessage(new char[0]) );
		
		assertNull( CribDragger.getCribForMessage("") );
	}
}
