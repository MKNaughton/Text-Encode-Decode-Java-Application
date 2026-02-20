package ie.atu.sw;

import java.io.*;

public class FileProcessor {

	// Arrays to store CSV mapping.
	private String[] words = new String[10_000]; // Array to keeps words and suffixes.
	private int[] codes = new int[10_000]; // Array to keep numeric codes
	private int Count = 0; // Counts the inputs loaded from CSV file.

	// instance variable for input and output file that can be changed through the
	// menu.
	private String inputPath = "./input.txt"; // default input file to read from
	private String outputPath = "./output.txt"; // default output file to save results

	// Instance variable for TextEncodingDecoding
	private TextEncodingDecoding encoder = null;

	// Method for menu option 1: to load csv mapping file - word to code mapping
	// into arrays.
	public void loadMappingFile(String filepath) {
		try {

			// File f = new File("./encodings-10000.csv");Instead of hard coding given cvs
			// file.
			File f = new File(filepath);// based on user input for file path with default option of cvs file given.

			// buffer-reader for input allows the code to read the text line by line and not
			// byte by byte.
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));

			String line = null;
			Count = 0; // Reset count when loading again.

			while ((line = br.readLine()) != null) {// reads each line from CSV file
				// check for csv format instead of vigenere .min_key_size and then parse csv
				// instead of encryption and decription.
				// If line has content greater then 0 and the line contains a comma
				if (line.length() > 0 && line.contains(",")) {
					// Then split the line with a comma and store word and the csv encoding number
					// in arrays.
					String[] parts = line.split(",");

					// If there are 2 parts and count is less then word.length(space in the array)
					if (parts.length == 2 && Count < words.length) {
						words[Count] = parts[0].trim(); // Then store word/suffix
						codes[Count] = Integer.parseInt(parts[1].trim()); // And store numeric code as Int
						Count++; // then move to the next position
					}
				}

			}
			// close the buffer-reader, make sure to close to avoid resource leak.
			br.close();

			// Instance variable - to pass loaded arrays to the encoder for text processing
			encoder = new TextEncodingDecoding(words, codes, Count);

			// Console feedback that there has been a successful load.
			System.out.println(
					ConsoleColour.GREEN + "[INFO] Loaded " + Count + " mapping from file" + ConsoleColour.RESET);
		} catch (Exception e) {
			e.printStackTrace();
			// Console feedback for failed loading
			System.out.println(
					ConsoleColour.RED + "[ERROR] Failed Loading" + Count + e.getMessage() + ConsoleColour.RESET);
		}
	}

	// method for menu to check if cvs is loaded by returning number count of words
	// from cvs file.
	public int getCount() {
		return Count;
	}

	// method for menu option 2 : setting input file path, menu calls this when user
	// specifies file
	public void setInputPath(String path) {
		this.inputPath = path; // Update which file to read from.
	}

	// method for menu option 3: setting output file path
	public void setOutputPath(String path) {
		this.outputPath = path; // Update where to save results

	}

	// method for menu option 5 : encode text file from CVS.
	public void encodeTextFile() {
		// Check if encoder file - CVS has been loaded
		if (encoder == null) {
			System.out.println(ConsoleColour.RED + "[ERROR]No mapping loaded - load first" + ConsoleColour.RESET);
			return;
		}

		System.out.println(ConsoleColour.YELLOW + "[DEBUG] Starting encoding!!!" + ConsoleColour.RESET);

		try {

			// First open file and count total lines in the input file to get calculation
			// for progress percentage shown in progress bar.

			BufferedReader counter = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath)));
			int totalLines = 0; // Counter for total lines in file.

			// Read through text first to count lines
			while (counter.readLine() != null) {
				totalLines++; // Increment each line found
			}
			counter.close(); // Close the line counter

			// Debug info to show file was opened successfully and showing how many lines
			// were found
			System.out.println(ConsoleColour.YELLOW + "[DEBUG] Files opened successfully!!!" + ConsoleColour.RESET);
			System.out.println(
					ConsoleColour.YELLOW + "[Debug] Total lines to process:" + totalLines + ConsoleColour.RESET);

			// open the input text file to be processed for encoding.
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath)));// using
																											// inputpath
																											// instead
																											// of
																											// hardcoding
																											// ./input.txt
			// open output file for writing
			FileWriter out = new FileWriter(outputPath);// using output path instead of hardcoding ./output.txt.

			String line = null;// Variable to keep each line read
			int processedLines = 0; // counter to track lines which have been processed.

			// Process each line of input file and track the progress
			while ((line = br.readLine()) != null) {
				// only encode lines that have text
				if (line.length() > 0) {
					processedLines++; // Increment the processed counter after encoding each line

					if (processedLines <= 3) {// show debug for fist 3 lines of text

						System.out.println(ConsoleColour.YELLOW + "[DEBUG] Processing line " + processedLines + ":"
								+ line.substring(0, Math.min(50, line.length())) + ConsoleColour.RESET);
					}

					// Use TextEncodingDecoding to encode the line
					String encoded = encoder.encode(line);

					if (processedLines <= 3) {
						System.out.println(ConsoleColour.YELLOW + "[DEBUG] Encode to:"
								+ encoded.substring(0, Math.min(50, encoded.length())) + ConsoleColour.RESET);
					}

					// out.write("Encode"+line.trim()+"\n");
					out.write(encoded + "\n");
				}
				// Update the progress bar every 2% so there is a total of 50 updates and update
				// on the last line to ensure the encoding was 100% successful.
				if (processedLines % Math.max(1, totalLines / 50) == 0 || processedLines == totalLines) {
					showProgress(processedLines, totalLines); // Call the progress display method.
				}
			}

			br.close();
			out.close();
			// Output feedback to the console encoding successful, the file was closed and
			// success message printed.
			System.out.println(ConsoleColour.YELLOW + "[Debug] Files closed succesfully" + ConsoleColour.RESET);
			System.out.println(ConsoleColour.GREEN + "[INFO] File encoded succesfully" + ConsoleColour.RESET);
			System.out.println(ConsoleColour.YELLOW + "[debug] success message printed" + ConsoleColour.RESET);

			// File error when app cannot locate the input file at the specified path
			// catching the error before the app goes to read and process the file.
		} catch (FileNotFoundException fnfe) {
			System.out.println(ConsoleColour.YELLOW + "[DEBUG] FileNotFoundException caught" + ConsoleColour.RESET);
			System.out.println(
					ConsoleColour.RED + "[ERROR] Input file not found:" + fnfe.getMessage() + ConsoleColour.RESET);
			return;// exits the method preventing continuing program with no file and using
					// unnessary operations.

			// To catch general input and output errors during file reading and processing
			// eg file corrupted.
			// catching errors during the file processing stage and stopping if file is
			// found but cannot be processed or there is a problem with the output file.

		} catch (IOException ioe) {
			System.out.println(ConsoleColour.YELLOW + "[DEBUG] IOException caught" + ConsoleColour.RESET);
			System.out
					.println(ConsoleColour.RED + "[ERROR] File I/O problem:" + ioe.getMessage() + ConsoleColour.RESET);
			return;// Stops processing as theres no point continuing if file operations are
					// failing.

			// catches all other unexpected errors not covered by filenot found and I/O
			// errors.

		} catch (Exception ex) {
			System.out.println(ConsoleColour.YELLOW + "[DEBUG] Exception caught" + ex.getClass().getSimpleName()
					+ ConsoleColour.RESET);
			System.out.println(ConsoleColour.RED + "[ERROR] Unexpected error:" + ex.getMessage() + ConsoleColour.RESET);
			ex.printStackTrace();
			return; // the program will exit rather then crashing
		}
	}

	// Method to display progress bar showing progress percentage.
	private void showProgress(int current, int total) {
		// Calculate percentage based on the total lines processed and the total lines
		// in the text.
		int percent = (current * 100) / total;

		// Visual progress bar setting for display
		int barlength = 50; // width of progress bar - 50 chars wide
		int filled = (current * barlength) / total; // how many chars to show as progress completed.

		// Build the visual progress bar string - StringBuilder
		StringBuilder bar = new StringBuilder("[");// Starting with the opening bracket.
		for (int i = 0; i < barlength; i++) {
			// Add # for completed and - for remaining.
			bar.append(i < filled ? "#" : "-");
		}
		bar.append("]" + percent + "%");// closing bracket and percentage number displayed.

		// Instead of printing multiple progress bar lines use \r to overwrite the
		// previous line to create one progress bar that updates.
		System.out.print(ConsoleColour.GREEN +"\r" + bar + ConsoleColour.RESET);

		// add newline when complete
		if (current == total) {
			System.out.println();
		}
	}

	// Menu option 6: Decode method for reversing the encoding in text and producing
	// decoded text.
	public void decodeTextFile() {

		// Same as encoding method - check if the encoder instance exists
		// If encoder is null it means CSV mapping file hasn't been loaded yet.
		if (encoder == null) {
			System.out.println(ConsoleColour.RED + "[ERROR]No mapping - please load first!" + ConsoleColour.RESET);
			return; // Exit method if no mapping file exists.
		}
		// Debug message to show decoding process has started
		System.out.println(ConsoleColour.YELLOW + "[DEBUG] Starting decoding process!" + ConsoleColour.RESET);

		try {
			// First process the text to get total line count in text as with encoding
			// method.
			BufferedReader counter = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath)));
			int totalLines = 0; // Counter for total lines in encoded text.

			// Read through the encoded file and count lines
			while (counter.readLine() != null) {
				totalLines++; // Increment the counter for each encoded line
			}
			counter.close(); // Close the counter.
			// Debug to show encoded file opened and lines counted.
			System.out.println(
					ConsoleColour.YELLOW + "[DEBUG] Files opened sucessfully for decoding !" + ConsoleColour.RESET);
			System.out.println(ConsoleColour.YELLOW + "[DEBUG] Total encoded lines to process:" + totalLines
					+ ConsoleColour.RESET);

			// Open the encoding file again and this time process the encoded text to
			// decoded text..
			// Use inputPath variable which was set through menu option 2
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath)));

			// open output file for writing decode text to.
			// Using inputPath variable set from menu option 3.
			FileWriter out = new FileWriter(outputPath);

			// Debug confirmation that both files input and output - opened without errors.
			System.out.println(
					ConsoleColour.YELLOW + "[DEBUG] Files opened successfully for decoding!" + ConsoleColour.RESET);

			String line = null;// Variable to store each line from encoded file.
			int processedLines = 0;// Counter to track lines have been decoded/processed.

			// Read each line from the encoding file until the end tracking the progress.
			while ((line = br.readLine()) != null) {
				if (line.length() > 0) {// Only process lines that have content skipping empty lines.
					processedLines++;// Increment processedlines counter after each encoded line has been decoded.

					// Show first 3 lines of decoded text for debug
					if (processedLines <= 3) {
						System.out.println(ConsoleColour.YELLOW + "[DEBUG] Decoding line " + processedLines + ":" + line
								+ ConsoleColour.RESET);

					}
					// Call the TextEncoding decode method to convert encode text to decoded text.
					String decoded = encoder.decode(line);

					// show decoded result for 3 lines for debug
					if (processedLines <= 3) {
						System.out
								.println(ConsoleColour.YELLOW + "[DEBUG] Decoded to:" + decoded + ConsoleColour.RESET);
					}
					// Write the decoded text to the output file
					out.write(decoded + "\n");

					// Update the progress bar as was done when encoding
					if (processedLines % Math.max(1, totalLines / 50) == 0 || processedLines == totalLines) {
						showProgress(processedLines, totalLines);// call the progress display method.
					}

				}
			}
			// close both files to prevent resource leaks
			br.close();
			out.close();

			// Debug feedback showing lines processed
			System.out.println(ConsoleColour.YELLOW + "[DEBUG] Processed " + processedLines + "lines for decoding"
					+ ConsoleColour.RESET);

			// Debug confirming files are closed properly
			System.out.println(ConsoleColour.YELLOW + "[DEBUG] Decode files closed Successfully" + ConsoleColour.RESET);

			// Success message to user
			System.out.println(ConsoleColour.GREEN + "[INFO] File successfully decoded" + ConsoleColour.RESET);

		} catch (FileNotFoundException fnfe) {
			// error for when file does not exist eg encoded file not created or wrong path.
			System.out.println(ConsoleColour.YELLOW + "[DEBUG] FileNotFoundException in decode" + ConsoleColour.RESET);
			System.out.println(
					ConsoleColour.YELLOW + "[DEBUG] Input file not found:" + fnfe.getMessage() + ConsoleColour.RESET);
			return;// Exit.

		} catch (IOException ioe) {
			// input and output errors
			System.out.println(ConsoleColour.YELLOW + "[DEBUG] Exception in decode" + ConsoleColour.RESET);
			System.out.println(
					ConsoleColour.RED + "[ERROR] Input file I/0 problem" + ioe.getMessage() + ConsoleColour.RESET);
			return;// Exit

		} catch (Exception ex) {
			// All other Errors
			System.out.println(ConsoleColour.YELLOW + "[DEBUG]Exception in decode" + ex.getClass().getSimpleName()
					+ ConsoleColour.RESET);
			System.out.println(
					ConsoleColour.YELLOW + "[DEBUG] Unexpected error:" + ex.getMessage() + ConsoleColour.RESET);
			ex.printStackTrace();// show full error details for debug
			return;// Exit.
		}

	}
}
