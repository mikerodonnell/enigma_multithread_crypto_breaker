package com.demo.crypto.enigma.exception;

/**
 * general exception class for an Engima machine being configured into a state that couldn't physically exist, or is otherwise invalid.
 * for example, two stecker cables being plugged into the same letter (same hole) on the Steckerbrett.
 * 
 * @author Mike O'Donnell  github.com/mikerodonnell
 */
public class InvalidConfigurationException extends RuntimeException {

	private static final long serialVersionUID = 1;
	
	public InvalidConfigurationException(String message) {
		super(message);
	}
}
