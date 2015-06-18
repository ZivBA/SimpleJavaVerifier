package oop.ex6.main;

import dataStructures.scope.Scope;
import parsing.exceptions.InvalidScopeException;
import parsing.exceptions.syntaxException;
import parsing.scopeParser.Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by rooty on 6/13/15.
 */
public class Sjavac {

	private static final int VALID_ARGUMENTS = 1;
	// all string constants for the man program.
	private static final String VALID_EXTENSION = ".sjava", INVALID_FILE = "1", VALID_FILE = "0",
			IO_EXCEPTION = "2";

	public static void main(String[] args) {
		// added number of arguments check
		// merged all tries to the same block, catch all applicable exceptions in the same place and
		// process accordingly.
		try {
			if (args.length != VALID_ARGUMENTS || !args[0].endsWith(VALID_EXTENSION)) {
				System.err.println(INVALID_FILE);

			}else {

				File sourceFile = new File(args[0]);

				if (sourceFile.length() == 0) {
					System.out.println(VALID_FILE);
				}

				String sourceString= cleanFile(sourceFile);
				parsing.syntax.syntaxValidator.validate(sourceString);
				Scope mainScope = new Scope(sourceString, null);

			}

			// for each exception type print the relevant number and message.
		} catch (IOException e) {

			System.err.println(IO_EXCEPTION);
			System.out.println( e.getMessage() );

		} catch (syntaxException | InvalidScopeException e) {
			System.err.println(INVALID_FILE);
			System.out.println(e.getMessage());
		}
	}

	public static String cleanFile(File sourceFile) throws IOException {
		Scanner tempScan = new Scanner(sourceFile);
		//return a string representation of the scanned file
		String stringFile = tempScan.useDelimiter("\\A").next();
		//delete all the legal comment from the file
		stringFile = stringFile.replaceAll("(\\A|\\n)//.*", "");
		//replace all the white space except the line skipping
		stringFile = stringFile.replaceAll("[^\\S\n]+", " ");
		//replace one or more line skipping by one line skipping
		stringFile = stringFile.replaceAll(" ?\n+ ?", "\n");
		stringFile = stringFile.replaceAll("\n+", "\n");
		//clean all the white space at the beginning and the end of the file
		stringFile = stringFile.trim();

		tempScan.close();
		return stringFile;
	}
}
