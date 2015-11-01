package com.demo.crypto.enigma.model;

public interface Steppable {
	
	public void step();
	
	public int getOffset();
	
	public void setOffset(int offset);

	public char getTurnoverCharacter();
	
}
