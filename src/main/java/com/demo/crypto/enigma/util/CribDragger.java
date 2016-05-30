package com.demo.crypto.enigma.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.demo.crypto.enigma.exception.NoMatchingCribException;
import com.demo.crypto.enigma.model.crib.Crib;
import com.demo.crypto.enigma.model.crib.NoSpecialOccurrences;
import com.demo.crypto.enigma.model.crib.ToCommandingAdmiralOfUBoats;
import com.demo.crypto.enigma.model.crib.ToFleetCruiserKoeln;

/**
 * encapsulates the functionality of a "dragging" cribs against an encrypted message, a type of <a href="https://en.wikipedia.org/wiki/Known-plaintext_attack">
 * known plaintext attach</a>.
 * 
 * @author Mike O'Donnell  github.com/mikerodonnell
 */
public class CribDragger {

	/**
	 * A List of all known Cribs to attempt to match against cipher text in the crib drag. The lowest index item is the longest crib, and the highest
	 * index is the shortest crib, so that matches can be attempted for longest (and therefore most reliable) cribs first.
	 */
	private final List<Crib> KNOWN_CRIBS;
	
	public CribDragger() {
		List<Crib> temp = new ArrayList<Crib>();
		temp.add(new NoSpecialOccurrences());
		temp.add(new ToCommandingAdmiralOfUBoats());
		temp.add(new ToFleetCruiserKoeln());
		KNOWN_CRIBS = Collections.unmodifiableList( temp );
	}
	
	
	public Crib getCribForMessage( final String cipherText ) throws NoMatchingCribException {
		if( StringUtils.isNotBlank(cipherText) )
			return getCribForMessage( cipherText.toCharArray() );
		
		throw new NoMatchingCribException(cipherText);
	}
	
	public Crib getCribForMessage( final char[] cipherText ) throws NoMatchingCribException {
		
		if( cipherText!=null && cipherText.length>0 ) {
			for( Crib crib : KNOWN_CRIBS ) { // ascending order is important here, Java for-each guarantees order as implemented by the Iterable interface, so we're good.
				
				int offset = 0;
				int maxOffset = cipherText.length - crib.getPlainText().length;
				
				if( crib.getStartIndex() != null ) {
					offset = crib.getStartIndex();
					maxOffset = offset;
				}
				
				while( offset <= maxOffset ) {
					for( int index=0; index<cipherText.length; index++ ) {
						
						// this crib can't exist at this index, Enigma never enciphers a character to itself!
						if( crib.getPlainText()[index] == cipherText[index+offset] )
							break;
						
						if(index==(crib.getPlainText().length-1)) {
							crib.setStartIndex(offset);
							return crib;
						}
					}
					
					offset++;
				}
			}
		}
		
		throw new NoMatchingCribException(new String(cipherText));
	}
	
}
