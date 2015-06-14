package oop.ex6.main;

import dataStructures.scope.Scope;
import parsing.exceptions.InvalidScopeException;
import parsing.scopeParser.Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by rooty on 6/13/15.
 */
public class Sjavac {


	public static void main(String[] args) {
		Scanner sourceScanner;

		File sourceFile = null;
		sourceFile = new File(args[0]);

		if (sourceFile.length() == 0) {
			//kill program?
		}

		try {
			sourceScanner = Parser.cleanFile(sourceFile);
			Scope mainScope = new Scope(sourceScanner, null);
		} catch (FileNotFoundException | InvalidScopeException e) {
			// kill program?
		}


	}


}
