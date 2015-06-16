package oop.ex6.main;

import dataStructures.scope.Scope;
import parsing.exceptions.InvalidScopeException;
import parsing.scopeParser.Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by rooty on 6/13/15.
 */
public class Sjavac {

	private static final Scope FIRST_SCOPE_HAS_NO_PARENT = null;
	private static final int VALID_ARGUMENTS = 1;

	public static void main(String[] args) {
		// number of arguments check
		try {
			if (args.length != VALID_ARGUMENTS) {
				throw new InvalidFileException();
			}
		} catch (InvalidFileException e) {
			// exit file, print 2 TODO maybe not try/catch?
			e.printErrorMessage();
		}

		Scanner sourceScanner;

		File sourceFile = new File(args[0]);

		if (sourceFile.length() == 0) {
			//kill program? or exception prints 2? or 1? empty file is valid...
		}

		try {
			sourceScanner = Parser.cleanFile(sourceFile);
			Scope mainScope = new Scope(sourceScanner, FIRST_SCOPE_HAS_NO_PARENT);
		} catch (FileNotFoundException | InvalidScopeException e) {
			// kill program?
		}


	}


}
