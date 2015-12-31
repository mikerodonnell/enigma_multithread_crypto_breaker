package com.demo.crypto.enigma.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;

import com.demo.crypto.enigma.model.crib.*;

public class CribDragger {

	private static final Collection<Crib> KNOWN_CRIBS;
	static {
		Collection<Crib> temp = new HashSet<Crib>();
		temp.add(new ToGeneral());
		temp.add(new NoSpecialOccurrences());
		KNOWN_CRIBS = Collections.unmodifiableCollection( temp );
	}
	
	
	public static Crib getCribForMessage( final String cipherText ) {
		if( StringUtils.isNotBlank(cipherText) )
			return getCribForMessage( cipherText.toCharArray() );
		
		return null;
	}
	
	public static Crib getCribForMessage( final char[] cipherText ) {
		
		if( cipherText!=null && cipherText.length>0 ) {
			for( Crib crib : KNOWN_CRIBS ) {
				
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
						
						if(index==(crib.getPlainText().length-1))
							return crib;
					}
					
					offset++;
				}
			}
		}
		
		return null;
	}
	
}
