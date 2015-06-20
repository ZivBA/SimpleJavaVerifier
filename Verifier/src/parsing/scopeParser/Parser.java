/**
 * Created by rooty on 6/11/15.
 */
package parsing.scopeParser;

import dataStructures.scope.Scope;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Parser {

	// recursive call root --> leaves, iterate throughout tree of scopes
	public static void startParsing(Scope mainScope) {

		for (String line : mainScope.getSrc()){
			// read line

			// if line is childScope#1
			startParsing(mainScope.getChild(1));
		}
	}


	private void variableCheck(){

	}


}
