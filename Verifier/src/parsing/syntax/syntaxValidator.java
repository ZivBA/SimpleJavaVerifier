package parsing.syntax;

import parsing.exceptions.syntaxException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Validator for checking that the file has legal syntax written on it.
 * The syntax that is checked are the different bracket types and the semicolon symbol.
 */
public class syntaxValidator {

	// Constants
	/**
	 * The chars to check in the validator
	 */
	private static final char BRACKET_OPEN = '(';
	private static final char BRACKET_CLOSE = ')';
	private static final char CURLY_OPEN = '{';
	private static final char CURLY_CLOSE = '}';
	private static final char SEMICOLON_END = ';';


	/**
	 * Static method for calling for the validator from outside.
	 * Creates the scanner for the file, cleans it from characters that dont need to be checked,
	 * checks if there were any illegal syntax entries and throws exception if needed.
	 *
	 * @param source the source file to validate
	 * @return Scanner object composing the file if it is legal
	 * @throws FileNotFoundException if there is an I/O problem with file
	 * @throws syntaxException       if there is illegal syntax in the file
	 */
	public static Scanner validate(File source) throws FileNotFoundException, syntaxException {
		Scanner sourceFile = new Scanner(source);
		sourceFile = cleanFile(sourceFile);
		searchForMissingSyntax(sourceFile);
		return sourceFile;
	}

	/**
	 * Cleans the file from any comments, redundant white spaces and line skips.
	 * @param sourceFile the Scanner composing the file to be cleaned.
	 * @return Scanner composing the file after cleaning.
	 * @throws FileNotFoundException if there is an I/O problem with the file.
	 */
	private static Scanner cleanFile(Scanner sourceFile) throws FileNotFoundException {
		//return a string representation of the scanned file
		String stringFile = sourceFile.useDelimiter("\\A").next();
		//delete all the legal comment from the file
		stringFile = stringFile.replaceAll("(\\A|\\n)//.*", "");
		//replace all the white space except the line skipping
		stringFile = stringFile.replaceAll("[^\\S\n]+", " ");
		//replace one or more line skipping by one line skipping
		stringFile = stringFile.replaceAll(" ?\n+ ?", "\n");
		stringFile = stringFile.replaceAll("\n+", "\n");
		//clean all the white space at the beginning and the end of the file
		stringFile = stringFile.trim();
		return new Scanner(stringFile);
	}

	/**
	 * Run over each line in the file and make sure syntax symbols are legal and in correct place in line.
	 * Round brackets need to be balanced for each line.
	 * Opening curly bracket and semicolon have to be at the end of line.
	 * Closing curly bracket has to be the only character in its own line.
	 * @param sourceFile the file for syntax checking.
	 * @throws syntaxException if there is any illegal syntax in place.
	 */
	private static void searchForMissingSyntax(Scanner sourceFile) throws syntaxException {
		String currentLine;
		int curlyBracketCounter = 0;
		while (sourceFile.hasNextLine()) { // run over whole file, line by line
			currentLine = sourceFile.nextLine();
			char[] lineAsCharArray = currentLine.toCharArray(); // look at line as char array

			int bracketCounter = 0;
			for (int i = 0; i < lineAsCharArray.length; i++) {
				switch (lineAsCharArray[i]) {
					case (BRACKET_OPEN):
						bracketCounter++;
						break;
					case (BRACKET_CLOSE):
						bracketCounter--;
						break;
					case (CURLY_OPEN): // may only be last char
						if (i != lineAsCharArray.length - 1) throw new syntaxException();
						else curlyBracketCounter++;
						break;
					case (CURLY_CLOSE): // has to have its own line
						if (lineAsCharArray.length != 1) throw new syntaxException();
						else curlyBracketCounter--;
						break;
					case (SEMICOLON_END):
						if (i != lineAsCharArray.length - 1) throw new syntaxException();
						break;
				}
				if (bracketCounter < 0 || curlyBracketCounter < 0) {
					throw new syntaxException(); // no opening brackets
				}
			} // after running over line, check that brackets are balanced and there is closer
			char lastChar = lineAsCharArray[lineAsCharArray.length-1];
			if (bracketCounter != 0 || lastChar != CURLY_OPEN && lastChar != SEMICOLON_END){
				throw new syntaxException();
			}
		} // after going over the file, check the curly brackets are balanced.
		if (curlyBracketCounter != 0) throw new syntaxException();
	}
}