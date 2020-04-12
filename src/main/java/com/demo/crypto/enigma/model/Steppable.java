package com.demo.crypto.enigma.model;

public interface Steppable {

	void step();

	int getOffset();

	void setOffset(int offset);
}
