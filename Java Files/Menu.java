
package ie.atu.sw;

import static java.lang.System.out;
import java.time.LocalDate;
import java.util.Scanner;

public class Menu {
	// Instance for fileProcessor
	private FileProcessor fp = new FileProcessor();

	// Scanner for user input.
	private Scanner s;
	private boolean keepRunning = true;// for while loop->keep running

	// Variables to keep file path.
	private String inputFilePath = "./input.txt";// input file
	private String outputFilePath = "./output.txt"; // output file
	private String mappingFilePath = "./encodings-10000.csv";// CSV file.

	// Constructor for scanner and loading CSV file
	public Menu() {
		s = new Scanner(System.in);
		// loads the csv mapping file as default.
		fp.loadMappingFile("./encodings-10000.csv");
	}

	// Start method for main menu while loop ->keeps running until quit option
	// chosen by user.
	public void start() {
		while (keepRunning) {
			showOptions();
			try {
				// User choice handled with switch statement.
				int choice = Integer.parseInt(s.next());
				s.nextLine();// makes sure the new line takes new input clears previous.
				switch (choice) {
				case 1 -> specifyMappingFile();
				case 2 -> specifyTextFile();
				case 3 -> specifyOutputFile();
				case 4 -> configureOptions();
				case 5 -> encodeTextFile();
				case 6 -> decodeTextFile1();
				case 7 -> quit();

				default -> out.println(ConsoleColour.RED + "[ERROR] Invalid Selection [1 - 7]" + ConsoleColour.RESET);
				}
			} catch (NumberFormatException e) {
				out.println(ConsoleColour.RED + "[ERROR] Invalid Selection - Please enter valid option []1 - 7"
						+ ConsoleColour.RESET);
				s.nextLine();// Clears the invalid number selection
			}
		}

		out.println(ConsoleColour.GREEN + "[INFO] Exiting...Bye!" + ConsoleColour.RESET);
	}

	// method for option 1 menu:to allow user input to specify mapping file
	private void specifyMappingFile() {
		out.println(ConsoleColour.GREEN + "[INFO] Specify Mapping File" + ConsoleColour.RESET);
		out.println(ConsoleColour.BLUE + "Default: ./encodings-10000.csv" + ConsoleColour.RESET);
		out.println(ConsoleColour.BLUE + " Press Enter to use default otherwise please enter new path>"
				+ ConsoleColour.RESET);

		String path = s.nextLine().trim();

		if (path.isEmpty()) {
			// use default csv file if empty
			mappingFilePath = "./encodings-10000.csv"; // set the default mapping file
			// otherwise
		} else {
			// allow for another path to be specified by the user through the menu.
			mappingFilePath = path;
		}
		// Reload mapping with new file
		fp.loadMappingFile(mappingFilePath);
		// give menu feedback
		out.println("Loaded mapping from:" + mappingFilePath);
	}
	// END OPTION 1 METHOD
	// ----------------------------------------------------------------

	// method for option 2: allow user to input to specify text file
	private void specifyTextFile() {
		out.println(ConsoleColour.GREEN + "[INFO] Specify Text File" + ConsoleColour.RESET);
		out.println("Current input file;" + inputFilePath);
		out.println("Enter input file path>");

		String path = s.nextLine().trim();

		if (!path.isEmpty()) {
			inputFilePath = path;
			fp.setInputPath(inputFilePath);
			// give menu feedback
			out.println(ConsoleColour.GREEN + "[INFO] Input file set to:" + inputFilePath + ConsoleColour.RESET);
		}
	}
	// END OPTION 2 METHOD ---------------------------------------------------

	// method for menu option 3: allow user to specify output
	private void specifyOutputFile() {
		out.println(ConsoleColour.GREEN + "[INFO] Specify Output File" + ConsoleColour.RESET);
		out.println("Current output file:" + outputFilePath);
		out.println(ConsoleColour.BLUE + "Enter output file path (default:./out.txt)>" + ConsoleColour.RESET);

		String path = s.nextLine().trim();

		if (!path.isEmpty()) {
			outputFilePath = path;
			fp.setOutputPath(outputFilePath);
			// give menu feedback
			out.println("[INFO] Input file set to:" + outputFilePath);
		}
	}
	// END OF OPTION 3
	// METHOD--------------------------------------------------------

	// Method for menu option 4:configure options from the command line menu
	private void configureOptions() {
		out.println(ConsoleColour.GREEN + "[INFO] Configure Options" + ConsoleColour.RESET);
		out.println("press the number 1 to show current Configuration details");
		out.println("Press the number 2 to reset to defaults");
		out.println("press 3 to return to main menu");
		out.println("Select option [1-3]>");

		try {
			int option = Integer.parseInt(s.next());
			s.nextLine();// line scanner

			switch (option) {
			case 1 -> showCurrentConfig();
			case 2 -> resetToDefault();
			case 3 -> returnToMainMenu();
			default -> out.println(
					ConsoleColour.RED + "[ERROR]Invalid input, Please select option [1-3]" + ConsoleColour.RESET);
			}
		} catch (NumberFormatException e) {
			out.println(ConsoleColour.RED + "[ERROR]Invalid input, Please select option [1-3]" + ConsoleColour.RESET);
		}
	}

	// method for menu option 4: option 1 -> to display the current config details
	private void currentConfig() {

		out.println(ConsoleColour.GREEN + "[INFO] Configure Options" + ConsoleColour.RESET);
		out.println("Press 1 to show current Configuration details");
		out.println("Press 2 to reset to defaults");
		out.println("Press 3 to return to main menu");
		out.println("Select option [1-3]>");

		try {
			int option = Integer.parseInt(s.next());
			s.nextLine();// line scanner

			switch (option) {
			case 1 -> showCurrentConfig();
			case 2 -> resetToDefault();
			case 3 -> returnToMainMenu();
			default ->
				out.println(ConsoleColour.RED + "[ERROR] Invalid input, Select options [1-3]" + ConsoleColour.RESET);
			}

		} catch (NumberFormatException e) {
			out.println(ConsoleColour.RED + "[ERROR] Invalid input, Select options [1-3]" + ConsoleColour.RESET);
		}
	}

	// method for menu option 4: option 2 -> rest to default.
	private void resetToDefault() {
		inputFilePath = "./input.txt ";
		outputFilePath = "./output.txt ";
		mappingFilePath = "./encodings-10000.csv";
		out.println(ConsoleColour.GREEN + "[INFO] Configuration reset to defaults" + ConsoleColour.RESET);
	}

	// method for menu option 4: case 3 -> Return to main menu
	private void returnToMainMenu() {
		out.println(ConsoleColour.GREEN + "[INFO] Returning to main menu!!" + ConsoleColour.RESET);
	}

	// END OF CONFIGURATION OPTION 4 METHODS
	// ------------------------------------------------------------------------

	// Method 5: encode Text file
	// Adapt findByID student app
	private void encodeTextFile() {
		out.println(ConsoleColour.GREEN + "[INFO] Encode Text File" + ConsoleColour.RESET);
		out.println("Input file:" + inputFilePath);
		out.println("Output file:" + outputFilePath);

//like when checking if student exists(student app) -> check if mapping file is loaded
		if (fp.getCount() == 0) {
			out.println(ConsoleColour.RED + "[ERROR] No mapping file loaded please load mapping file first!!"
					+ ConsoleColour.RESET);
			return;
		}
		out.println(ConsoleColour.GREEN + "[INFO]Starting encoding process!" + ConsoleColour.RESET);

		// call method for encoding from FileProcessor
		fp.encodeTextFile();

		out.println(ConsoleColour.GREEN + "[INFO] Encoding Complete.Output saved to:" + outputFilePath
				+ ConsoleColour.RESET);
	}
	// END OF MENU OPTION 5
	// METHODS--------------------------------------------------------------

	// Method for option 6: decode text file (opposite to encode)

	private void decodeTextFile1() {
		out.println(ConsoleColour.GREEN + "[INFO] Decode Text File" + ConsoleColour.RESET);
		out.println("Input file:" + inputFilePath);
		out.println("Output file:" + outputFilePath);

		// like when checking if student exists(student app) -> check if mapping file is
		// loaded
		if (fp.getCount() == 0) {
			out.println(ConsoleColour.RED + "[ERROR] No mapping file loaded please load mapping file first!!"
					+ ConsoleColour.RESET);
			return;
		}
		out.println(ConsoleColour.GREEN + "[INFO]Starting decoding process!" + ConsoleColour.RESET);

		// call method for encoding from FileProcessor
		fp.decodeTextFile();
		out.println(ConsoleColour.GREEN + "[INFO] decoding Complete.Output saved to:" + outputFilePath
				+ ConsoleColour.RESET);

	}
	// END OF MENU OPTION 6
	// METHODS-----------------------------------------------------------

//method for option 7: quit application 
	// adapt from student app: keepRunning = false
	private void quit() {
		out.println(ConsoleColour.GREEN + "[INFO] Quit? (yes/no)>" + ConsoleColour.RESET);
		String confirm = s.next();
		if (confirm.equalsIgnoreCase("yes")) {
			keepRunning = false;
		}
	}
	// END MENU OPTION
	// 7-------------------------------------------------------------------------

	// private void selectOption() {
	// out.println("[INFO] Select Option");
//}

	// Method to show current configuration - Like GetTotal in student app-display
	// information.

	private void showCurrentConfig() {
		out.println("======Current Configuration======");
		out.println("Mapping File:" + mappingFilePath);
		out.println("Input File:" + inputFilePath);
		out.println("Output File" + outputFilePath);
		out.println("Words Loaded:" + fp.getCount());
		out.println("=====================================");
	}

	private void showOptions() {
		// Given code from runner class to display menu
		System.out.println(ConsoleColour.WHITE);
		System.out.println("************************************************************");
		System.out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
		System.out.println("*                                                          *");
		System.out.println("*              Encoding Words with Suffixes                *");
		System.out.println("*                                                          *");
		System.out.println("************************************************************");
		System.out.println("(1) Specify Mapping File");
		System.out.println("(2) Specify Text File to Encode");
		System.out.println("(3) Specify Output File (default: ./out.txt)");
		System.out.println("(4) Configure Options");
		System.out.println("(5) Encode Text File");
		System.out.println("(6) Decode Text File");
		System.out.println("(7) Quit");
		System.out.println(ConsoleColour.BLACK_BOLD_BRIGHT);
		System.out.println("Select an Option [1-7]>");

	}
}
