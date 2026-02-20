package ie.atu.sw;

import java.io.*;

public class TextEncodingDecoding {
	// Arrays to keep mapping from csv file

	private String[] words; // String Array keeps words and suffixes from the CVS
	private int[] codes; // Int Array keeps numeric codes from cvs
	private int count; // keeps count of what has been loaded.

	// Constructor - Vigenere constructor takes a key - fileprocessor will call this
	// constructor instead of key will pass in reference not copies of arrays.
	// both classes will share the same arrays in memory helping with large input
	// loads and processing time, help prevent program from crashing.
	public TextEncodingDecoding(String[] words, int[] codes, int count) {
		this.words = words; // keeps reference /lookup address to words array.
		this.codes = codes; // keeps reference/look up address to arrays.
		this.count = count; // keeps total number of what has been loaded.
	}

	// method to encode the text into the numeric code using word suffix mapping.
	
	public String encode(String plainText) throws Exception {

		// using stringbuilder (better then string concatenation- (caesarchiper))
		StringBuilder sb = new StringBuilder();

		// split text into words at white space instead of character by
		// character(Vigenere).
		// To allow for the cvs mapping to identify individual words,not counting spaces
		// or line breaks, to code and decode not encoding and decoding each character.
		String[] inputWords = plainText.split("\\s+");// split on whitespace

		// loop through each word instead of characters -(Vigenere looping through
		// characters)
		for (int i = 0; i < inputWords.length; i++) {
			// look up the code for the word -(Vigenere- GetEncryptedCharacter())
			String wordCode = getEncodeWord(inputWords[i]);
			// append to stringbuilder.
			sb.append(wordCode);
			// add space between codes except at the end to keep word codes seperate in
			// encoded text.
			if (i < inputWords.length - 1) {
				sb.append(" ");
			}
		}
		// return the encoded string
		return sb.toString();
	}

//Method to normalize the text to lowercase and take away non-alphanumeric char before lookup 
//Ref:Downey,A.B(2012) Think Java:How to Think like a Computer Scentist, Ch.7 - Strings
//Ref: Null check avoids NullPointerException(Think Java, Ch. 10 Handling Errors)	
//Ref: W3xschools Java String.ReplaceAll() and W3Schools Java String.toLowerCase().	

	private String normalize(String word) {
		if (word == null)
			return "";// Null check to avoid exception.
		return word.toLowerCase().replaceAll("[^a-z0-9]", "");// remove special characters
	}

	// method to encode single words - exact matches like student id search in
	// student app.
	private String getEncodeWord(String word) {
		// Apply normalization
		String cleaned = normalize(word);

		// Loop through the array
		for (int i = 0; i < count; i++) {

			// Check for null to avoid NullPointerException - prevent crashes when searching
			// through partially filled arrays
			if (words[i] != null && words[i].equals(cleaned)) {
				// When matches are found covert int code to string and return
				return String.valueOf(codes[i]);
			}
		}

		// Method to deal with no matches by splitting into prefix and suffix.
		// work through full word length splitting to find correct splitpoint .
		// Ref:W3schools Java String.substring.
		// Ref: Downey,A.B (2012).Think Java:How to Think like a Computer Scentist, Ch.7
		// - Strings, Ch.6 - Loops

		for (int splitPoint = 1; splitPoint < cleaned.length(); splitPoint++) {
			// Take the right prefix which has a match (index num of word, splitpoint).
			String prefix = cleaned.substring(0, splitPoint);

			// Take the remaining part of the word this becomes the suffix
			String suffix = cleaned.substring(splitPoint);

			// append @@ to suffix to match csv code suffix
			String suffixKey = "@@" + suffix;

			// variables to store codes when found
			int prefixCode = -1;// -1 not found yet
			int suffixCode = -1;

			// Search through the arrays for prefix and suffix - like student app searching
			// student array
			for (int i = 0; i < count; i++) {
				if (words[i] != null) {
					// then check if word matches prefix
					if (words[i].equals(prefix)) {
						// then save the corresponding int code in code array.
						prefixCode = codes[i];
					}
					// check if word with append @@ matches
					if (words[i].equals(suffixKey)) {
						// Then save to the corrresponding int code to codes array.
						suffixCode = codes[i];

					}
				}

			}
			// If both the prefix and suffix append are found create double code

			if (prefixCode != -1 && suffixCode != -1) {
				// then return in double code formatt
				return "[" + prefixCode + "," + suffixCode + "]";

			}
		}
		return "0";
	}

	// Decode method , reverse look up- splits encoded string into word codes using
	// mapping - not every character like Vigenere's decript

	public String decode(String encodedText) throws Exception {
		// Debug to show what encoded line are being decoded - for trouble shooting but
		// causing spam!
		// System.out.println("[DEBUG] Starting decode for line:" +
		// encodedText.substring(0, Math.min(50, encodedText.length())));

		// StringBuilder better than string concatenation.
		StringBuilder sb = new StringBuilder();

		// split on spaces to get the individual codes
		String[] inputCodes = encodedText.split(" ");
		// Debug to show how many codes found in the line- good for troubleshooting but
		// causes console spam!
		// System.out.println("[DEBUG]Found" + inputCodes.length + "codes to decode");

		// Loop through each code and decode it back to a word.
		for (int i = 0; i < inputCodes.length; i++) {
			// call decode method
			String decoded = decodeCode(inputCodes[i]);
			// normalize decoded words before appending
			decoded = normalize(decoded);
			// add decoded word to result
			sb.append(decoded);
			// add space between words except the last
			if (i < inputCodes.length - 1) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}

	// Method to decode each code - single and pairs in brackets.
	private String decodeCode(String codeStr) {
		// method to check double codes with brackets
		if (codeStr.startsWith("[") && codeStr.endsWith("]")) {
			// Remove the brackets to get the inner code
			String inner = codeStr.substring(1, codeStr.length() - 1);
			// Split on the comma to separate prefix and suffix
			String[] parts = inner.split(",");

			// StringBuilder to build the decoded word
			StringBuilder result = new StringBuilder();

			// Decode each part
			for (String part : parts) {
				// convert string to int for lookup.
				int code = Integer.parseInt(part.trim());
				// get word for corresponding code
				String word = getWordByCode(code);

				// if it is a suffix remove the @@
				if (word.startsWith("@@")) {
					word = word.substring(2);
				}
				// add to result
				result.append(word);
			}
			// Return the combined word
			return result.toString();
		} else {
			// return the single codes
			int code = Integer.parseInt(codeStr);
			return getWordByCode(code);
		}
	}

	// Method for finding word by its code - reverse lookup
	// Like getStudentByID() but searching by code instead
	private String getWordByCode(int code) {
		// if code equals 0 return [???]
		if (code == 0)
			return "[???]";

		// search through code array - like student array in student app.
		for (int i = 0; i < count; i++) {
			if (codes[i] == code) {
				return words[i];
			}
		}
		// not found return ???
		return "[???]";
	}
}
