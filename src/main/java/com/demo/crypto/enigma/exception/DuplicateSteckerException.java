package com.demo.crypto.enigma.exception;

/**
 * an Exception to be thrown if two stecker cables are plugged into the same letter (same hole) on the Steckerbrett.
 *
 * @author Mike O'Donnell  github.com/mikerodonnell
 */
public class DuplicateSteckerException extends InvalidConfigurationException {

	private static final long serialVersionUID = 1;

	public DuplicateSteckerException(String message) {
		super(message);
	}
}
