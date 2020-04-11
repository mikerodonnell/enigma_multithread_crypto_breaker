package com.demo.crypto.enigma.model.crib;

/**
 * A sample crib found at the beginning of messages from the Naval M4 Enigma.
 *
 * @author Mike O'Donnell  github.com/mikerodonnell, credit <a href="http://google.com">http://google.com</a>
 * credit <a href="https://enigma.hoerenberg.com/">www.enigma.hoerenberg.com</a>
 */
public class ToCommandingAdmiralOfUBoats extends Crib {

	public ToCommandingAdmiralOfUBoats() {
		// this crib is actually from the Naval M4 Enigma, and in reality wouldn't be used for decryption with an Army M3 Enigma machine as we've implemenented.
		super(0, "ANXKOMXADMXUUUBOOTE", "To Commanding Admiral of U-Boats");
	}
}
