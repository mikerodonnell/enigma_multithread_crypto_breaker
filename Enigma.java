
/** ------------------------------------------------------------------------ 
 * Enigma.java
 *      This is an Enigma machine decryption program u
 *
 * Class: CS 107
 * Lab: Englebert Humberdink, Mon. 5:00 AM
 * System: Eclipse 3.3.2, Java sdk 1.6, Windows XP
 *
 * @author  Dale Reed
 * @version April 12, 2009
 * 
 * Running program gives:
 * 
		


 * ----------------------------------------------------------------------------
 */

// import Java libraries that are needed
import java.util.Scanner;       // used for console input

// the following are needed to implement reading from the dictionary
import java.io.IOException;     // for IOException
import java.util.ArrayList;     // Used to create ArrayLists dictionary use
import java.io.*;               // Used for IOException, File
 

// Declare the class
public class Enigma
{
    // Fields that can be accessed anywhere in the class go here
    Scanner keyboard = new Scanner( System.in);     // used to read user input
    // Declare a dynamically allocated ArrayList of Strings for the dictionary.
    // The dictionary can hold any number of words.
    ArrayList<String> dictionary;
    
    // Values for three rotors
    char[] innerRotor =  {' ','G','N','U','A','H','O','V','B','I','P','W','C','J','Q','X','D','K','R','Y','E','L','S','Z','F','M','T'};
    char[] middleRotor = {' ','E','J','O','T','Y','C','H','M','R','W','A','F','K','P','U','Z','D','I','N','S','X','B','G','L','Q','V'};
    char[] outerRotor =  {' ','B','D','F','H','J','L','N','P','R','T','V','X','Z','A','C','E','G','I','K','M','O','Q','S','U','W','Y'};

    // Create index arrays corresponding to each of the above, so that a letter can be used directly
    // as an index and doesn't require searching.  In other words, rather than having to search within
    // the innerRotor array to find 'A' at index position 4, we have a innerRotorIndex array that has
    // an entry for each ASCII character, and at position 'A' (which is 65) we will store the value 4.
    // This will speed up the program, since we won't need to search through the array for each letter
    // for each lookup, but rather can go directly there using the letter as the index.
    int[] innerRotorIndex = new int[128];
    int[] middleRotorIndex = new int[128];
    int[] outerRotorIndex = new int[128];
    
    // Starting rotor position is chosen by the user, and modifies the starting rotor values
    char innerRotorStartingLetter = ' ';
    char middleRotorStartingLetter = ' ';
    char outerRotorStartingLetter = ' ';
    
    // Fields to store best case found
    int bestNumberOfDictionaryHits = 0;	// will range from 0 to # of input words
    String bestDecryptedString = "";	// will store best decrypted string found
    char bestInnerRotorSetting = ' ';	// will store rotor start letter for best solution found
    char bestMiddleRotorSetting = ' ';  // will store rotor start letter for best solution found
    char bestOuterRotorSetting = ' ';   // will store rotor start letter for best solution found
    

    public static void main(String[] args) throws IOException  
    {
        // create an instance of this class
        Enigma EnigmaInstance = new Enigma();        
        // call a non-static method to do everything else
        EnigmaInstance.mainLoop();    
        
        System.out.println("\n" +
		   "Exiting program...\n");
    }
    
    
    //-------------------------------------------------------------------------
    // mainLoop() - display identifying information and run main loop with menu
    //      The words "throws IOException" have to do with dictionary error 
    //      handling.
    //
    void mainLoop() throws IOException 
    {
        // First take care of creating and initializing the dictionary
        // Define the instance of the dictionary ArrayList
        dictionary = new ArrayList<String>();
        // Now fill the dictionary array list with words from the dictionary file
        //readInDictionaryWords();
            	
        String cipherText = "";      // stores user Input in main loop
        char[] cipherTextArray;		 // starts with the cipherText; Eventually will store the decrypted text
        
        // Display identifying information
        System.out.println( "Author: Dale Reed \n" +
                            "Class: CS 107, Spring 2009 \n" +
                            "Program #5: Enigma \n" +
                            "TA: Billie Joe Armstrong, T 6:00 AM \n" +
                            "April 12, 2009\n");                                   
                            
        // Prompt for input to be used
        System.out.println("Choose from the following options:");
        System.out.println("     1. Encode some text ");
        System.out.println("     2. Decode using user-entered rotor starting values ");
        System.out.println("     3. Decode by trying all possible rotor combinations ");
        System.out.println("     4. Exit program ");
        System.out.print("Your choice: ");
        String menuChoice = keyboard.nextLine();
        
        // handle user input for "exit"
        if( menuChoice.equals("4") ) {
        	System.out.println("Exit was chosen.");
        	// skip rest of code
        	System.exit( 0);        
        }
        
        // Handle menu options 1 or 2, both of which prompt the user for the starting rotor
        // values.  Start with the code that initializes the rotor arrays that will be used 
        // in encoding/decoding.
        if( (menuChoice.equals("1")) || (menuChoice.equals("2")) ) {
        	// Encode or Decode using user-entered values
        	
	        // prompt for and store starting rotor values
        	promptForStartingRotorValuesAndRotateRotors();
	        
        }//end if( (menuChoice.equals...
        
        if( menuChoice.equals("1") ) {
        	// Selected "1" to encode some text
        	encodeText();
        	// skip rest of code
        	System.out.println("\n" +
        					   "Exiting after encoding text...");
        	System.exit( 0);
        }
        else if( menuChoice.equals("2") || menuChoice.equals("3")) {
        	// Selected "2" to decode using user-entered values, or
        	// selected "3" to decode trying all possible rotor starting positions.
        	
        	// Prompt for cipherText to use
        	System.out.print("Enter the cipherText to be decoded: ");
        	cipherText = keyboard.nextLine();
        	// convert to upper case and make it into a character array
        	cipherTextArray = cipherText.toUpperCase().toCharArray();
        	
        	if( menuChoice.equals("2") ) {
        		// Decode using user-entered values
	        	// send the cipherText, which will be decoded and returned in place
	        	decodeText( cipherTextArray);
	        	// display answer
	        	System.out.println("Decoded text is: " + String.copyValueOf( cipherTextArray));
        	}
        	else {
        		// menuChoice was "3", which should try all possible starting rotor positions
        		tryAllRotorPositions( cipherTextArray);
        		
        		// display best decoded text found
	        	System.out.println("\n" +
	        					   "Decoded text is: " + bestDecryptedString + "\n");
	        	// for some reason the stored rotor settings are incorrect, though the decrypted string is correct. ???
	        	//				   "Using rotor settings: " + bestInnerRotorSetting + bestMiddleRotorSetting + bestOuterRotorSetting);  	    			

        	}//end else
        	
        }//end else if( menuchoice.equals...
        else {
			System.out.println("Invalid menu option chosen.  Please re-run program");
        	// skip rest of code
        	System.exit( 0);
        }
 
    }//end mainLoop()
 
    
    // Prompt for and store the starting rotor values.
    // Do some sanity check evaluation of the input values as well.
    public void promptForStartingRotorValuesAndRotateRotors()
    {
        System.out.print("Enter the inner, mid, and outer rotor starting letters (e.g. ABC) or press enter for default: ");
        String userInput = keyboard.nextLine();
        if( userInput.length() == 0) {
        	// user wants defaults of blanks to be the starting letters
        	System.out.println("Default of blanks used for the starting letters.");
            innerRotorStartingLetter = ' ';
            middleRotorStartingLetter = ' ';
            outerRotorStartingLetter = ' ';
        }
        else {   
        	// there was user input to select the starting letters for each rotor
        	
	        // convert userInput to all upper case
	        userInput = userInput.toUpperCase();
	        // extract characters for starting rotor values
	        innerRotorStartingLetter = userInput.charAt( 0);
	        middleRotorStartingLetter = userInput.charAt( 1);
	        outerRotorStartingLetter = userInput.charAt( 2);
	        
	        // Sanity check
	        if( (userInput.length() != 3) || 
	        	(innerRotorStartingLetter != ' ' && !Character.isLetter( innerRotorStartingLetter) ) ||
	        	(middleRotorStartingLetter != ' ' && !Character.isLetter( middleRotorStartingLetter) ) ||
	        	(outerRotorStartingLetter != ' ' && !Character.isLetter( outerRotorStartingLetter) ) 
	           ) {
	        	// There was a problem, so exit program
	        	System.out.println("*** Invalid rotor letters.  Exiting program...");
	        	System.exit( -1);
	        }        	
        }//end else
        
        // Rotate rotor arrays to reflect the user-selected starting letter
        rotateRotors( innerRotorStartingLetter, middleRotorStartingLetter, outerRotorStartingLetter);

    }//end promptForAndStoreStartingRotorValues()
    
    
    // Rotate rotors based on starting letters.  Then reinitialize the arrays of rotor index values
    // used to speed up program execution.
    public void rotateRotors(char innerRotorStartingLetter, 
    						 char middleRotorStartingLetter, 
    						 char outerRotorStartingLetter)
    {
    	// rotate rotors to the given starting letter
        rotateRotorToStartingCharacter( innerRotorStartingLetter, innerRotor);
        rotateRotorToStartingCharacter( middleRotorStartingLetter, middleRotor);
        rotateRotorToStartingCharacter( outerRotorStartingLetter, outerRotor);  
        
        // Set rotor index values based on the rotor characters. Though not essential,
        // this speeds up the program since we can jump right to a letter and don't need
        // to search for it within each rotor.
        setRotorIndexValues(innerRotorIndex, innerRotor);
        setRotorIndexValues(middleRotorIndex, middleRotor);
        setRotorIndexValues(outerRotorIndex, outerRotor);
    }//end rotateRotors()
    
    
    // rotate the rotor letters to reflect the user's choice of rotor starting position.
    public void rotateRotorToStartingCharacter( char theLetter, char[] theRotor)
    {
    	// first make a copy of the original array so we have the values to use
    	// while we modify the original
    	char[] theRotorCopy = new char[ theRotor.length];
       	for( int i=0; i<theRotor.length; i++) {
       		theRotorCopy[ i] = theRotor[ i];
       	}
       	     	
    	int letterPosition = -1;
    	// find the letter in the rotor
    	for( int i=0; i<theRotorCopy.length; i++) {
    		if( theRotor[ i] == theLetter) {
    			letterPosition = i;
    			break;	// we found it, so don't need to search any more
    		}
    	}//end for( int i...
    	
    	// Copy over the letters from the copy into the original, which we're changing.
    	// Start the copy at the letter which will now be at the beginning, and wrap around
    	// back to the beginning of the array while copying.
    	for( int i=0; i<theRotorCopy.length; i++) {
    		theRotor[ i] = theRotorCopy[ (i + letterPosition)%theRotor.length];
    	}
      	    	
    }//end rotateToMakeFirstCharacter
    
    
    // Create index arrays corresponding to each of the above, so that a letter can be used directly
    // as an index and doesn't require searching.  In other words, rather than having to search within
    // the innerRotor array to find 'A' at index position 4, we have a innerRotorIndex array that has
    // an entry for each letter of the alphabet, at at position 'A' (which is 65) we store the value 4.   
    public void setRotorIndexValues( int[] rotorIndex, char[] theRotorCharacters)
    {
    	// set default values to -1 for index values.  This way if an invalid character is ever encountered
    	// the program will stop due to an index out of bounds error.
    	for( int i=0; i<rotorIndex.length; i++) {
    		rotorIndex[ i] = -1;
    	}//end for( int i...
    	
    	for( int i=0; i<theRotorCharacters.length; i++) {
    		rotorIndex[ theRotorCharacters[i]] = i;	// store the index where this character is found
    	}//end for( int i...
    	
    }//end setRotorIndexValues()
    
    
    // Encode user input.
    // Go through array of characters, encrypting each one as we go.  To encrypt do the following:
    // 1. Find the character on the inner rotor, and store the outer rotor character in that position
    // 2. Find the outer rotor character on the middle rotor
    // 3. Find the new outer rotor character corresponding to that middle rotor character
    // Rather than have arrays of rotor characters and rotate those arrays, here we use a
    // formula to calculate which rotor character should be used based on offsets to the 
    // current character position.
   public void encodeText()
    {
        System.out.print("Enter the text to be encoded: ");
        String plainText = keyboard.nextLine();    // pause for user input
        // convert to all upper case
        plainText = plainText.toUpperCase(); 
        // convert to array of char
        char[] plainTextArray = plainText.toCharArray();
        
        int plainTextCharValue;
        int innerRotorCharIndex;
        int middleRotorCharIndex;
        char outerRotorCharacter;
        
        // Go through array of characters, encrypting each one as we go.  
        for( int i=0; i<plainTextArray.length; i++) {
        	// get the character from the plainText array
        	plainTextCharValue = (int)plainTextArray[i];
        	// find the position where that character is on the inner rotor
            innerRotorCharIndex = innerRotorIndex[ plainTextCharValue];
            // find the corresponding character at that position on the outer rotor. Add an extra
            // increment to reflect the inner rotor rotation as we go
            outerRotorCharacter = outerRotor[ (innerRotorCharIndex + (i%27)) %27];
            // Find that character on the middle rotor
            middleRotorCharIndex = middleRotorIndex[ (int)outerRotorCharacter];
            // Finally find the character on the outer rotor that matches that middle rotor position. Note that
            // each time the inner rotor makes a complete revolution, the middle rotor advances once.  Each of
            // these middle rotor rotations means the middle rotor then corresponds to one character *after* it
            // would otherwise on the outer rotor, so add this amount.
             outerRotorCharacter = outerRotor[ (middleRotorCharIndex + (i/27)%27) %27];
           
            System.out.println("~~~~~~~~~ " + outerRotorCharacter);
            
        }//end for( int i...
           
    }//end promptForEncoding()
    
 
    // Given an encrypted string as an array of char, decrypt it.
	// Go through array of characters, decrypting each one as we go.  To decrypt do the following:
	// 1. Find the character on the outer rotor, and store the middle rotor character in that position
	// 2. Find that middle rotor character on the outer rotor
	// 3. Find the new inner rotor character corresponding to that outer rotor character
	// Rather than have arrays of rotor characters and rotate those arrays, here we use a
	// formula to calculate which rotor character should be used based on offsets to the 
	// current character position.   
	public void decodeText( char[] cipherTextArray)
	{	
		int cipherTextCharValue;
		int innerRotorCharIndex;
		char innerRotorCharacter;
		char middleRotorCharacter;
		int outerRotorCharIndex;
		int numberOfRotorShifts;	// stores intermediate values in calculating offsets
		int incrementIndex;			// amount we have to add (using mod) to implement subtraction

		
		// Go through array of characters, decrypting each one as we go.  
		for( int i=0; i<cipherTextArray.length; i++) {
			// get the character from the plainText array
			cipherTextCharValue = (int)cipherTextArray[i];
			// find the position where that character is on the outer rotor
		    outerRotorCharIndex = outerRotorIndex[ cipherTextCharValue];
		    
			// Store the middle rotor character in that same index position. Subtract from the index
		    // the number of times the middle rotor has been rotated. Since we can't subtract and "wrap
		    // around" in an array, instead we do the equivalent by adding (27 - the_amount_to_subtract)
		    numberOfRotorShifts = (i/27)%27;	// amount to be subtracted
		    incrementIndex = 27 - numberOfRotorShifts;	// turn subtraction into addition
		    middleRotorCharacter = middleRotor[ (outerRotorCharIndex + incrementIndex) %27];
			// Find that middle rotor character on the outer rotor
		    outerRotorCharIndex = outerRotorIndex[ middleRotorCharacter];
		    
			// Find the new inner rotor character corresponding to that outer rotor character.
		    // Subtract from the inner rotor index the number of times the inner rotor has been rotated. 
		    // Since we can't subtract and "wrap around" in an array, instead we do the equivalent 
		    // by adding (27 - the_amount_to_subtract)
		    numberOfRotorShifts = i%27;
		    incrementIndex = 27 - numberOfRotorShifts;
		    innerRotorCharacter = innerRotor[ (outerRotorCharIndex + incrementIndex) %27];
		   
		    cipherTextArray[ i] = innerRotorCharacter;
		    
		}//end for( int i...		
	}//end decodeText
	
	
	// Try all possible rotor positions for automatic decryption
	public void tryAllRotorPositions( char[] cipherTextArray)  	// text to be decrypted
	{
		int numberOfWordsFound = 0;		// tracks how many words found in dictionary for each possible string
		
		// Create an array of 27 characters (' ' plus all upper-case characters) to use from which to select rotor positions
		char[] rotorSelectArray = new char[ 27];
		rotorSelectArray[ 0] = ' ';
		for( int i=1; i<rotorSelectArray.length; i++) {
			rotorSelectArray[ i] = (char)('A' + i-1);	//subtract 1 because 0th array character is already ' ', then 'A' is at 1st position
		}//end for( int i...
		
		// Now try all alphabetic combinations (26 x 26 x 26 = 17576 of them)
		boolean finishedSearching = false;		// boolean flag to allow exiting search once solution is found
		
		System.out.print("Trying rotor combinations starting with: ");
		for( int i = 0; i<rotorSelectArray.length && !finishedSearching; i++) {
			System.out.print( rotorSelectArray[ i] + " ");		// To monitor progress display first letter of rotor combinations
			
			for( int j = 0; j<rotorSelectArray.length && !finishedSearching; j++) {
				for( int k = 0; k<rotorSelectArray.length && !finishedSearching; k++) {
					// rotate rotors to reflect this combination of starting letters
					rotateRotors(  rotorSelectArray[ i], rotorSelectArray[ j],  rotorSelectArray[ k]);	
			        
					// Decode using these rotor positions
			        decodeText( cipherTextArray);
			        
			        // See how many words are found.  First create a String out of the char array
			        String plainText = String.copyValueOf(cipherTextArray);
			        // do the dictionary lookup, returning the number of words found
			        numberOfWordsFound = dictionaryLookup( plainText);
			        // Store the new best case, if this is it
			        storeBestCase( plainText, numberOfWordsFound, rotorSelectArray[ i], rotorSelectArray[ j],  rotorSelectArray[ k]);

				}//end for( int k...
			}//end for( int j...
		}//end for( int i...
		
	}//end tryAllRotorPositions
	
	
    // Create a single String from an array of words, which is useful for printing & debugging
    public String createStringFromWordsArray( String[] wordsArray)
    {
        String newString = "";
        for( int i=0; i<wordsArray.length; i++) {
        	newString = newString + wordsArray[ i] + " ";
        }    	
        
        return newString;		// return the newly created String
    }//end createStringFromWordsArray(...
    
    
    // Given a String, break it into individual words.  Lookup each word in the dictionary
    // and keep track of how many are found.
    public int dictionaryLookup( String theWords)
    {
    	String[] wordsArray = theWords.split(" ");
    	// Chain off to dictionary lookup that is expecting an array of words
    	return dictionaryLookup( wordsArray);
    }
    
    
    public int dictionaryLookup( String[] wordsArray)
    {	    
        // count how many of these words are in the dictionary
        int wordFoundCount = 0;
        for( int i=0; i<wordsArray.length; i++) {
        	// convert word to lower case for dictionary lookup.  Also verify that word length
        	// is > 0, otherwise empty Strings are considered found in the dictionary
        	if( wordExists( wordsArray[ i].toLowerCase()) && (wordsArray[ i].length() > 0) ) {
        		wordFoundCount++;
        		// For debugging:
        		//System.out.println("Found " + wordsArray[ i]);
        	}
        }//end for( int i...
        
        return wordFoundCount;
    }//end dictionaryLookup...
    
    
    // If we have a new best case scenario, store the values so we can use it to decode
    public void storeBestCase(String cipherText,
    						  int numberOfDictionaryHits,
    						  char innerRotorSetting,
    						  char middleRotorSetting,
    						  char outerRotorSetting)
    {
		// Store information if we have a new best case
		if( numberOfDictionaryHits > bestNumberOfDictionaryHits) {
			// we have a new best case, so store these values
		    bestNumberOfDictionaryHits = numberOfDictionaryHits;	// store best number of dictionary hits
		    bestDecryptedString = new String( cipherText);			// store best decrypted text
		    bestInnerRotorSetting = innerRotorSetting;				// store best rotor settings
		    bestMiddleRotorSetting = middleRotorSetting;  
		    bestOuterRotorSetting = outerRotorSetting;   			
	        // display values for debugging
		    /*
			System.out.println("\n" + 
					      	   "New best case with " + numberOfDictionaryHits + " hits. " +
							   "Rotors set to: " + innerRotorSetting + middleRotorSetting + outerRotorSetting + ". " +
					           " PlainText: " + bestDecryptedString +
					           "\n");
	        */
		}//end if( numberOfDictionaryHits...
		
	}//end storeBestCase(...)
    
	
    
    // Read in the words to create the dictionary.
    // It throws an IOException, which is a way to gracefully handle errors
    // should there be a problem reading from the input.
    public void readInDictionaryWords() throws IOException
    {
        // Define a Scanner to read from an input file.  Note that the name of
        // the file given in the code below MUST match the actual filename of 
        // the dictionary file.  This file should be in the same directory
        // as the source code for WordCross.java
        File dictionaryFile = new File("dictionary.txt");    // declare the file
        // print the directory where this program expects to find dictionary
        System.out.println(System.getProperty("user.dir"));
        // ensure file exists and is in the correct directory
        if( ! dictionaryFile.exists()) {
            System.out.println("*** Error *** \n" +
                               "Your dictionary file has the wrong name or is " +
                               "in the wrong directory.  \n" +
                               "Aborting program...\n\n");
            System.exit( -1);    // Terminate the program
        }
        Scanner inputFile = new Scanner( dictionaryFile);
        
        // while there are words in the input file, add them to the dictionary
        while( inputFile.hasNext()) {
            dictionary.add( inputFile.nextLine() );
        }
    }//end createDictionary()
    
    
    // Allow looking up a word in dictionary, returning a value of true or false
    public boolean wordExists( String wordToLookup)
    {
        if( dictionary.contains( wordToLookup)) {
            return true;    // words was found in dictionary
        }
        else {
            return false;   // word was not found in dictionary    
        }
    }//end wordExists
    
    
}//end Class Enigma