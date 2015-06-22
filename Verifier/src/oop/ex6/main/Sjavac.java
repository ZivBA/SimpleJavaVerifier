package oop.ex6.main;

import dataStructures.scope.Scope;
import dataStructures.scope.exceptions.ScopeException;
import dataStructures.vars.exceptions.VariableException;
import parsing.exceptions.ParsingException;
import parsing.syntax.SyntaxValidator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Main program class - the Sjavac simplidied Java verifier is used to parse and validate the code in an
 * .sjava file. it does so by building a scope tree for the code, traversing the tree from top to bottom,
 * excluding methods, validating assignments and declerations, checking method calls against their method
 * declerations and parsing the methods when called. finally once all the "runable" code is verified,
 * iterate over the remaining methods that were declared but not called.
 *
 * usage: java Sjavac &lt; fileToProcess.sjava &gt;
 * expected output: '0' if the file is valid, '1' + exception details if the file is invalid, '2' if there
 * was an I/O exception somewhere.
 */
public class Sjavac {

	// all string constants for the main program.
	private static final int VALID_ARGUMENTS = 1;
	private static final String VALID_EXTENSION = ".sjava", INVALID_FILE = "1", VALID_FILE = "0",
			IO_EXCEPTION = "2";

	public static void main(String[] args) {
		// since the program runs until finished, except when exceptions are thrown, the entire main code
		// is wrapped in a try/catch. if the attempt succeeds, output '0', shoudl an exception be thorwn,
		// catch it and print the appropriate message.
		try {
			// check that only one argument is passed, and that it ends with ".sjava"
			if (args.length != VALID_ARGUMENTS || !args[0].endsWith(VALID_EXTENSION)) {
				System.err.println(INVALID_FILE);
			} else {

				// create a File obj from program agument, if size = 0 it's vacuously valid
				File sourceFile = new File(args[0]);
				if (sourceFile.length() == 0) {
					System.out.println(VALID_FILE);
				}

				// run the simple SyntaxValidator on the sourceFile
				ArrayList<String> validatedSource = SyntaxValidator.validate(sourceFile);

				// start Scope parsing - create the mainScope and recursively build it's tree.
				Scope mainScope = new Scope(validatedSource, null);

				// start parsing the main scope.
				parsing.scopeParser.Parser.startParsing(mainScope);
				System.out.println(VALID_FILE); // program ends here if valid

			}

			// for each exception type print the relevant output result to sys.out, and the error message
			// to sys.err
		} catch (IOException e) {
			System.out.println(IO_EXCEPTION);
			System.err.println(e.getMessage());
		} catch (ParsingException | ScopeException | VariableException e) {
			System.out.println(INVALID_FILE);
			System.err.println(e.getMessage());
		}
	}
}
