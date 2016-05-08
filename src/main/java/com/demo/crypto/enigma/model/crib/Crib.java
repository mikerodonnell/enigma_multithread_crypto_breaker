package com.demo.crypto.enigma.model.crib;

/**
 * wrapper around a plain text fragment, or crib, known to have a high probability of existing within encrypted text.
 * 
 * @author Mike O'Donnell  github.com/mikerodonnell
 */
public abstract class Crib {

	/** the index at which this crib is applicable. typical values are 0 (cribs that are always the start of the message) and null (cribs that can appear anywhere) */
	protected Integer startIndex;
	
	/** the crib plain text, in German */
	protected final String plainText;
	
	/** the English translation of the German plain text. may be used for user interface, logging, etc. */
	protected final String translation;
	
	protected Crib( String plainText, String translation ) {
		this(null, plainText, translation);
	}
	
	protected Crib( Integer startIndex, String plainText, String translation ) {
		this.startIndex = startIndex;
		this.plainText = plainText;
		this.translation = translation;
	}
	
	
	public Integer getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	
	public char[] getPlainText() {
		return plainText.toCharArray();
	}
	
	public String getTranslation() {
		return translation;
	}
	
	@Override
	public String toString() {
		return plainText;
	}
}
