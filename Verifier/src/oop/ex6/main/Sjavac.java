package oop.ex6.main;

import dataStructures.scope.Scope;
import parsing.exceptions.InvalidScopeException;
import parsing.exceptions.SyntaxException2;
import parsing.syntax.SyntaxValidator2;

import java.io.File;
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

				Scanner validatedSource = SyntaxValidator2.validate(sourceFile);
				Scope mainScope = new Scope(validatedSource, null);
				parsing.scopeParser.Parser.startParsing(mainScope);

			}

			// for each exception type print the relevant number and message.
		} catch (IOException e) {

			System.err.println(IO_EXCEPTION);
			System.out.println( e.getMessage() );

		} catch (SyntaxException2 | InvalidScopeException e) {
			System.err.println(INVALID_FILE);
			System.out.println(e.getMessage());
		}
	}

}
