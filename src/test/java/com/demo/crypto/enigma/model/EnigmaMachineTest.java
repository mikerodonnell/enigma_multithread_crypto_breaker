package com.demo.crypto.enigma.model;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.demo.crypto.enigma.model.EnigmaMachine;

public class EnigmaMachineTest {
	
	@Test
	public void testEncryptDefaultPositions() {
		
		EnigmaMachine enigmaMachine = new EnigmaMachine();
		
		// this is a known documented example, when the rotors start in AAA position with no ring offset and no plugboard
		assertEquals( "BDZGO", enigmaMachine.encrypt("AAAAA") );
		
		// these are sample encryptions for other implementations, trusting that they're correct
		enigmaMachine.reset();
		assertEquals( "BJELRQZVJWARXSNBXORSTNCFME", enigmaMachine.encrypt("ABCDEFGHIJKLMNOPQRSTUVWXYZ") );
		enigmaMachine.reset();
		assertEquals( "ABCDEFGHIJKLMNOPQRSTUVWXYZ", enigmaMachine.decrypt("BJELRQZVJWARXSNBXORSTNCFME") );
		
		enigmaMachine.reset();
		assertEquals( "ILBDAAMTAZ", enigmaMachine.encrypt("HELLOWORLD") );
		enigmaMachine.reset();
		assertEquals( "HELLOWORLD", enigmaMachine.decrypt("ILBDAAMTAZ") );
	}
	
	
	@Test
	public void testEncryptUnsteckered() {
		
		EnigmaMachine enigmaMachine = new EnigmaMachine( new char[]{'M', 'O', 'D'} );
		
		// these are sample encryptions for other implementations, trusting that they're correct
		assertEquals( "JCQMIGLINIHRPOPSMWBXTWZCZJ", enigmaMachine.encrypt("ABCDEFGHIJKLMNOPQRSTUVWXYZ") );
		enigmaMachine.reset();
		assertEquals( "ABCDEFGHIJKLMNOPQRSTUVWXYZ", enigmaMachine.decrypt("JCQMIGLINIHRPOPSMWBXTWZCZJ") );
		
		enigmaMachine.reset();
		assertEquals( "LTGSDUIQET", enigmaMachine.encrypt("HELLOWORLD") );
		enigmaMachine.reset();
		assertEquals( "HELLOWORLD", enigmaMachine.encrypt("LTGSDUIQET") );
	}
	
	
	@Test
	public void testEncrypt() {
		
		Map<Character, Character> steckeredPairs = new HashMap<Character, Character>();
		steckeredPairs.put('C', 'N');
		EnigmaMachine enigmaMachine = new EnigmaMachine( new char[]{'M', 'O', 'D'}, steckeredPairs );
		
		// these are sample encryptions for other implementations, trusting that they're correct
		assertEquals( "JNIMIGLICIHRPAPSMWBXTWZNZJ", enigmaMachine.encrypt("ABCDEFGHIJKLMNOPQRSTUVWXYZ") );
		enigmaMachine.reset(); 
		assertEquals( "ABCDEFGHIJKLMNOPQRSTUVWXYZ", enigmaMachine.decrypt("JNIMIGLICIHRPAPSMWBXTWZNZJ") );
	}
}