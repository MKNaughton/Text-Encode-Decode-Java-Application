package ie.atu.sw;

public class StringBuilder {
	// Arrays to keep characters, word or code to build strings.
	private char[] text = null;
	private int index = 0;

	// constructor to set array
	public StringBuilder() {
		text = new char[4];
	}

	// Constructor with starting string , creating stringbuilder from existing
	// string - loading csv data.
	public StringBuilder(String s) {
		this();
		// add string to stringbuilder
		append(s);
	}

	// method to add the string to the end of the Stringbuilder instead of using
	// string concationation.
	public StringBuilder append(String s) {
		// loop through each char in string .
		for (int i = 0; i < s.length(); i++) {
			// add each char one by one.
			append(s.charAt(i));
		}
		return this;
	}

	// Method for adding a single char to stringbuilder
	public StringBuilder append(char c) {
		// check if more space is needed before adding
		ensureCapacity();
		// add char at the current index
		text[index] = c;
		// move to the next position
		index++;

		return this;

	}

	// method to covert number to text -adding int as string

	public StringBuilder append(int num) {
		// convert int to string and append using string append method
		return append(String.valueOf(num));
	}

	// method to grow text array
	private void ensureCapacity() {
		// if index is bigger then or equal to length of array
		if (index >= text.length) {
			// then create larger array by doubling - trading space for time scales better.
			char[] temp = new char[text.length * 2];
			for (int i = 0; i < text.length; i++) {
				temp[i] = text[i];
			}
			// replace the text array with larger temp array
			text = temp;
		}
	}

	// method which now does the opposite
	public String toString() {
		return new String(text, 0, index);
	}
}
