package com.demo.crypto.enigma.model;

// YRUHQSLDPXNGOKMIEBFZCWVJAT
public class WideBReflector extends AbstractEnigmaReflector {

	private static final char[] SUBSTITUTIONS = {'Y', 'R', 'U', 'H', 'Q', 'S', 'L', 'D', 'P', 'X', 'N', 'G', 'O', 'K', 'M', 'I', 'E', 'B', 'F', 'Z', 'C', 'W', 'V', 'J', 'A', 'T'};
	
	public WideBReflector() {
		super(SUBSTITUTIONS);
	}
	
	public WideBReflector(char[] substitutions) {
		super(substitutions);
	}
	
	@Override
	public String toString() {
		return "Enigma \"Wide\" B Reflector.";
	}
}
