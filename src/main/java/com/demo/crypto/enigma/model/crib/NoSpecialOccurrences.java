package com.demo.crypto.enigma.model.crib;

/**
 * KEINEBESONDERENEREIGNISSE ("No special occurrences") was a common crib used in Enigma deciphering. using this extended version for higher accuracy, and
 * specifying that it would be the beginning of the message (index 0).
 * 
 * @author Mike O'Donnell  github.com/mikerodonnell
 */
public class NoSpecialOccurrences extends Crib {
	
	public NoSpecialOccurrences() {
		super( 0, "KEINEBESONDERENEREIGNISSEXZUXBERICHTENXHEUTE", "No special occurrences to report today." );
	}

}
