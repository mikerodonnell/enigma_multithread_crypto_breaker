package com.demo.crypto.enigma.model;

/**
 * a representation of a single Enigma steckerbrett (plug board) cable.
 *
 * @author Mike O'Donnell  github.com/mikerodonnell
 */
public class SteckerCable {

	private char input;
	private char output;

	public SteckerCable(char input, char output) {
		this.input = input;
		this.output = output;
	}

	public char getInput() {
		return input;
	}

	public void setInput(char input) {
		this.input = input;
	}

	public char getOutput() {
		return output;
	}

	public void setOutput(char output) {
		this.output = output;
	}

	@Override
	public String toString() {
		return (input + " => " + output);
	}

	@Override
	public boolean equals(Object testObject) {
		if (testObject != null && testObject instanceof SteckerCable) {
			SteckerCable testSteckerCable = (SteckerCable) testObject;
			return testSteckerCable.getInput() == input && testSteckerCable.getOutput() == output || testSteckerCable.getInput() == output && testSteckerCable.getOutput() == input;

		}

		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + input;
		result = prime * result + output;
		return result;
	}
}
