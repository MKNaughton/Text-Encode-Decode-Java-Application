# Text-Encode-Decode-Java-Application
## Description
This is a console-based Java application that encodes and decodes local text files using a CSV word-to-code mapping system. The application reads text documents from the user 's file system, converts words to numeric codes using a 9999-entry dictionary, and can reverse the process to restore original text. Words not found in the dictionary are handled through an intelligent prefix-suffix splitting algorithm, making it useful for text compression , obfuscation, or educational purposes in understanding encoding systems.Users interact through a menu-driven interface where they can specify local file paths for input/output files, load the required CSV mapping file, and execute encoding or decoding operations with real-time progress feedback displayed in the console.

## Project Overview

This application processes text files word by word rather than character-by-character,using arrays to store word to code mappings loaded from a CSV file. The encoding algorithm searches for exact word matches first, then falls back to prefix-suffix splitting when words aren't found in the dictionary.Files are parsed line by line with BufferedReader, and the application provides a 7 option menu interface for complete workflow control including configuration management and progress tracking.

## System Architecture

The application is organised across six classes with clear separation of concerns:

![UML Class Diagram](images/TextDecodeEncode%20UML.png)

### Core Components

- **Runner** - Application entry point that launches the menu system
- **Menu** - Command-line interface for user interaction and input validation
- **FileProcessor** - Manages file I/O operations, CSV loading, and coordinates encoding/decoding
- **TextEncodingDecoding** - Core encoding/decoding logic with prefix-suffix splitting algorithm
- **StringBuilder** - Custom implementation using char arrays with dynamic resizing
- **ConsoleColour** - ANSI escape sequences for colour-coded console output

### Key Design Decisions

- **Word-based processing** rather than character-based encryption. Text is split on whitespace (`\s+`) to identify individual words for lookup in the mapping arrays.
- **Prefix-suffix splitting** handles words not found in the CSV by iterating through split points: "running" becomes "run" + "ning", "ru" + "nning", etc. The suffix is matched by appending "@@" to align with CSV format.
- **Normalization** converts all text to lowercase and strips non-alphanumeric characters before lookup, significantly improving encoding success rates from ~53% to much higher.
- **Arrays passed by reference** between FileProcessor and TextEncodingDecoding to avoid copying large datasets and improve memory efficiency.
- **Double code format** `[prefixCode,suffixCode]` for words requiring prefix-suffix splits, with single codes for direct matches.

## Technologies

- **Language:** Java
- **I/O:** BufferedReader, FileWriter
- **Data Structures:** Arrays (String[], int[])
- **Build:** Executable JAR

## Features

### Encoding Process
- Splits input text into words
- Normalizes each word (lowercase, strips special characters)
- Searches arrays for exact matches
- Falls back to prefix-suffix splitting if no match found
- Outputs numeric codes in format: `3352 193 5610` or `[211,9495]` for split words
- Unknown words encoded as `0`

### Decoding Process
- Parses encoded text to identify single codes vs double codes (brackets)
- Performs reverse lookup in code arrays
- Strips "@@" prefix from suffixes
- Reconstructs original words from prefix-suffix pairs
- Unknown codes decoded as `[???]`

### User Interface
- **7-option menu** for complete workflow control
- **Customizable file paths** - no hardcoded values
- **Default file options** - `./encodings-10000.csv`, `./input.txt`, `./output.txt`
- **Configuration viewer** - displays current settings and loaded word count
- **Progress bar** - real-time percentage display with visual indicator `[####----] 45%`

### Error Handling
- **FileNotFoundException** - validates paths before processing
- **IOException** - handles corrupted files gracefully
- **NullPointerException** - null checks on all array operations
- **NumberFormatException** - catches invalid menu selections

### Performance Optimizations
- BufferedReader for efficient file reading
- First-pass line counting for accurate progress calculation
- Progress updates at 2% intervals (50 total updates)
- Debug output limited to first 3 lines to reduce console spam
- Empty line handling maintains output structure

## Getting Started

git clone https://github.com/MKNaughton/TextEncodingReadMe.git
cd TextEncodingReadMe

### Prerequisites
- Java Development Kit (JDK 8 or higher)
- `encodings-10000.csv` mapping file (9,999 word/suffix entries)

### Running the Application

**Option 1: Using the JAR file (recommended)**
```bash
java -jar encoder.jar
```

**Option 2: Running from source**
1. Compile the source code
```bash
javac ie/atu/sw/*.java
```

2. Run the application
```bash
java ie.atu.sw.Runner
```

### Using the Application

**Menu Options:**
1. **Specify Mapping File** - Load CSV word-to-code mapping (default: `./encodings-10000.csv`)
2. **Specify Text File to Encode** - Set input file path
3. **Specify Output File** - Set output file path (default: `./output.txt`)
4. **Configure Options** - View current configuration, reset to defaults
5. **Encode Text File** - Convert text to numeric codes
6. **Decode Text File** - Convert codes back to text
7. **Quit** - Exit application

**Example Workflow:**
```
1. Load mapping file (CSV)
2. Set input file path: ./input.txt
3. Set output file path: ./encoded.txt
4. Execute encoding
5. Change input to: ./encoded.txt
6. Change output to: ./decoded.txt
7. Execute decoding
```

## Implementation Details

### Encoding Algorithm

The encoding process follows this workflow:
1. Text is split into words using `String.split("\\s+")`
2. Each word is normalized with `normalize()` method
3. Arrays are searched for exact matches
4. If no match, prefix-suffix splitting begins:
   - Loop from splitPoint 1 to word.length()
   - Create prefix: `word.substring(0, splitPoint)`
   - Create suffix: `word.substring(splitPoint)`
   - Append "@@" to suffix for CSV format matching
   - Search arrays for both parts
   - If both found, return `[prefixCode,suffixCode]`
5. If no encoding possible, return `0`

### Decoding Algorithm

The decoding process reverses encoding:
1. Split encoded text on spaces to get individual codes
2. Check if code starts with `[` and ends with `]` (double code)
3. For double codes:
   - Strip brackets
   - Split on comma
   - Look up each code in arrays
   - Strip "@@" from suffix entries
   - Concatenate prefix and suffix
4. For single codes:
   - Look up code directly in arrays
5. Unknown codes return `[???]`

### Normalization Method
```java
private String normalize(String word) {
    if (word == null) return "";
    return word.toLowerCase().replaceAll("[^a-z0-9]", "");
}
```

This method prevents crashes from null values and standardizes text for consistent lookups by removing punctuation, capitalization differences, and special characters.

### Custom StringBuilder

Implemented from scratch using char arrays rather than String concatenation:
- Starts with capacity of 4 characters
- Doubles capacity when full (trading space for time)
- Supports `append(String)`, `append(char)`, `append(int)`
- Converts to String via `toString()` method

## Author

Marykerin Naughton
