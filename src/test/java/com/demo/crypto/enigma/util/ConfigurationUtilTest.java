package com.demo.crypto.enigma.util;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class ConfigurationUtilTest {

	@Test
	public void testGenerateRandomRotorPositions() {
		assertEquals( 3, ConfigurationUtil.generateRandomRotorPositions().length );
		
		// verify that #generateRandomRotorPositions() returns something different each time. this will fail every 26^3 though.
		// org.junit.Assert implements #assertArrayEquals() but no #assertArrayNotEquals(), so we have to write it this way.
		assertFalse( Arrays.equals(ConfigurationUtil.generateRandomRotorPositions(), ConfigurationUtil.generateRandomRotorPositions()) );
	}
	
	@Test
	public void testGenerateRandomSteckers() {
		assertTrue( ConfigurationUtil.generateRandomSteckers(0).isEmpty() );
		assertEquals( 1, ConfigurationUtil.generateRandomSteckers(1).size() );
		assertEquals( 2, ConfigurationUtil.generateRandomSteckers(2).size() );
		assertEquals( 10, ConfigurationUtil.generateRandomSteckers(10).size() );
	}
}
