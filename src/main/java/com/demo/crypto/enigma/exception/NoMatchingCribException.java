package com.demo.crypto.enigma.exception;

public class NoMatchingCribException extends RuntimeException {

	public NoMatchingCribException(String cipherText) {
		super("No known crib matches the cipher text: " + cipherText);
	}
	
}
