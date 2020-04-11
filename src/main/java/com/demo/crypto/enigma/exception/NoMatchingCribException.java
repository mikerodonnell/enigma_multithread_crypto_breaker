package com.demo.crypto.enigma.exception;

/**
 * an Exception to be thrown if no known plaintext crib aligns with the encrypted message, and thus a crib drag attack is not possible.
 *
 * @author Mike O'Donnell  github.com/mikerodonnell
 */
public class NoMatchingCribException extends RuntimeException {

	private static final long serialVersionUID = 1;

	public NoMatchingCribException(String cipherText) {
		super("No known crib matches the cipher text: " + cipherText);
	}
}
