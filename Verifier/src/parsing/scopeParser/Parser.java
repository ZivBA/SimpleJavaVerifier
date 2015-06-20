/**
 * Created by rooty on 6/11/15.
 */
package parsing.scopeParser;

import dataStructures.scope.Scope;
import dataStructures.vars.VariableObject;
import parsing.RegexDepot;
import parsing.exceptions.DuplicateAssignmentException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
				variableDeclareLine(scope, line);
			} else if (variableAssignMatch.matches()){
				variableAssignLine(scope, line); // TODO will this cover method call line?
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

	private static void variableDeclareLine(Scope scope, String line) {
		// get the type (make sure legal), for each sections between the comma's, check if assignment,
		// make varObject and try to add it.
		boolean isFinal = false;
		Matcher variableDeclarationMatch = RegexDepot.VARIABLE_DECLARATION_PATTERN.matcher(line);
		if (variableDeclarationMatch.group(1).contains("final")){ // TODO maybe matches is better?
			isFinal = true;
		}
		String type = variableDeclarationMatch.group(2);

		// split by commas and keep without final&type
		String[] namesAndAssignment = variableDeclarationMatch.group(3).split(",");
		for (String var : namesAndAssignment){
			Matcher assignment = RegexDepot.VARIABLE_ASSIGNEMENT_PATTERN.matcher(var);
			Matcher declaration = RegexDepot.VARIABLE_PATTERN.matcher(var);
			if (assignment.matches()){
				// get the name and value
				String varName = assignment.group(1);
				String value = assignment.group(2);
				// TODO what if value is another var? handle this in variableObject
				VariableObject varObject = new VariableObject(varName, type, value, isFinal);
				try {
					scope.addVar(varObject);
				} catch (DuplicateAssignmentException e) {
					throw new DuplicateAssignmentException(varObject);
				}
			} else if (declaration.matches()){
				// create the var and try to push it to scopes storage
			} else {
				// throw new not valid declaration.
			}
		}

		String name;
		String value;

	}

	private static void variableAssignLine(Scope scope, String line) {

	}

	private static void parseMethod(Scope scope) {

	}


}
