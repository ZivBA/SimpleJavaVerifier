/**
 * Created by rooty on 6/11/15.
 */
package parsing.scopeParser;

import dataStructures.scope.Scope;
import parsing.RegexDepot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;

public class Parser {

	// recursive call root --> leaves, iterate throughout tree of scopes
	public static void startParsing(Scope scope) {
		ArrayList<String> fileLines = scope.getSrc();
		for (String line : fileLines){
			// read line

			// cases, var declare, var assign, method declare/if while scope,
			Matcher variableDeclarationMatch = RegexDepot.VARIABLE_DECLARATION_PATTERN.matcher(line);
			Matcher variableAssignMatch = RegexDepot.VARIABLE_ASSIGNEMENT_PATTERN.matcher(line);
			Matcher methodScopeMatch = RegexDepot.METHOD_PATTERN.matcher(line);
			Matcher conditionScopeMatch = RegexDepot.CONDITION_PATTERN.matcher(line);

			if (variableDeclarationMatch.matches()){
				variableDeclareLine(line);
			} else if (variableAssignMatch.matches()){
				variableAssignLine(line); // TODO will this cover method call line?
			} else if (methodScopeMatch.matches()) {
				// skip, this is parsed at end.
				continue; // TODO maybe check that the method scope exists
			} else if (conditionScopeMatch.matches()){
				startParsing(scope.getAllChildren().pollFirst()); // FIFO
			} else {
				// throw new parsing exception
			}

		} // finished running over all lines but the method lines
		while (!scope.getAllMethods().isEmpty()){
			parseMethod(scope.getAllMethods().pollFirst());
		}
	}

	private static void variableDeclareLine(String line) {
		// get the type (make sure legal), for each sections between the comma's, check if assignment,
		// make varObject and try to add it.
		String type = line.substring(0, line.indexOf(" ")); // TODO should we check that this is legal?

		String name;
		String value;

	}

	private static void variableAssignLine(String line) {

	}

	private static void parseMethod(Scope scope) {

	}


}
