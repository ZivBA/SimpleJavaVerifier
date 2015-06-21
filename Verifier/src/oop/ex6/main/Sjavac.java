package oop.ex6.main;

import dataStructures.scope.Scope;
import dataStructures.vars.VariableException;
import parsing.exceptions.*;
import parsing.syntax.SyntaxValidator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

				ArrayList<String> validatedSource = SyntaxValidator.validate(sourceFile);
				Scope mainScope = new Scope(validatedSource, null);
				printScopeTree(mainScope,0);
				parsing.scopeParser.Parser.startParsing(mainScope);

			}

			// for each exception type print the relevant number and message.
		} catch (IOException e) {

			System.err.println(IO_EXCEPTION);
			System.out.println( e.getMessage() );

		} catch (SyntaxException | InvalidScopeException e) {
			System.err.println(INVALID_FILE);
			System.out.println(e.getMessage());
		} catch (invalidMethodException e) {
			e.printStackTrace();
		} catch (VariableException e) {
			// cont?
		}
	}

	private static void printScopeTree(Scope root, int depth){
		for (int i=0; i<depth+1; i++){
			System.out.print("	");
		}
//		System.out.println("Scope at depth "+depth+": "+ root);
		for (Scope child : root.getAllChildren()){
			for (int i=0; i<depth+2; i++){
				System.out.print("	");
			}
			System.out.println("-- "+child);
			printScopeTree(child,depth+1);
		}for (Scope method : root.getAllMethods()){
			for (int i=0; i<depth+2; i++){
				System.out.print("	");
			}
			System.out.println("-- "+method);
			printScopeTree(method,depth+1);
		}
	}
}
