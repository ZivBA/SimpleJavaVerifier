/**
 * Created by rooty on 6/11/15.
 */
package parsing.scopeParser;

import dataStructures.scope.Method;
import dataStructures.scope.Scope;
import dataStructures.scope.exceptions.InvalidConditionsException;
import dataStructures.scope.exceptions.ScopeException;
import dataStructures.scope.exceptions.invalidMethodException;
import dataStructures.vars.VariableObject;
import dataStructures.vars.exceptions.IllegalAssignmentException;
import dataStructures.vars.exceptions.VariableException;
import parsing.RegexDepot;
import parsing.exceptions.SyntaxException;

import java.util.ArrayList;
import java.util.regex.Matcher;

import static dataStructures.vars.VariableObject.VarTypeAndValue.BOOLEAN;

public class Parser {
	private static final String ARG_DELIM = " *, *";
	private static final String CONDITION_DELIM = "\\|\\||&&";

	// recursive call root --> leaves, iterate throughout tree of scopes
	public static void startParsing(Scope scope) throws VariableException, ScopeException, SyntaxException {
		ArrayList<String> fileLines = scope.getSrc();
		// parse all methods - store them in memory to be ready for method calls during procedural reading.


		for (String line : fileLines) {
			// read line

			// cases, var declare, var assign, method declare/if while scope,
			Matcher variableDeclarationMatch = RegexDepot.VARIABLE_DECLARATION_PATTERN.matcher(line);
			Matcher variableAssignMatch = RegexDepot.VARIABLE_ASSIGNEMENT_PATTERN.matcher(line);
			Matcher methodScopeMatch = RegexDepot.METHOD_PATTERN.matcher(line);
			Matcher conditionScopeMatch = RegexDepot.CONDITION_PATTERN.matcher(line);
			Matcher methodCallMatch = RegexDepot.METHOD_CALL_PATTERN.matcher(line);

			if (variableDeclarationMatch.matches()) {
				variableDeclareLine(scope, line);
			} else if (variableAssignMatch.matches()) {
				variableAssignLine(scope, line, variableAssignMatch);
			} else if (methodCallMatch.matches()) {
				methodCallChecker(scope, methodCallMatch);
			} else if (methodScopeMatch.matches()) { // should match the pattern left in line
				// skip, this is parsed at end.
				continue;
			} else if (conditionScopeMatch.matches()) {
				if (!conditionsChecker(scope, conditionScopeMatch.group(2))) {
					throw new InvalidConditionsException();
				}
				startParsing(scope.getAllChildren().pollFirst()); // FIFO
			} else {
				throw new SyntaxException();
			}

		} // finished running over all lines but the method lines

		for (Method temp : scope.getAllMethods()) {
			methodArgParse(temp);
			startParsing(temp);
		}
	}

	private static void methodArgParse(Method method) throws VariableException {
		if (!method.getArguments().isEmpty()) {
			return;
		}
		String[] arguments = method.getArgString().split(ARG_DELIM);

		for (String argument : arguments) {

			if (argument.length() != 0) {
				Matcher varDecMatch = RegexDepot.VARIABLE_DECLARATION_PATTERN.matcher(argument.trim());
				if (!varDecMatch.matches()) {
					throw new IllegalAssignmentException(argument);
				}
				method.addMethodArgument(new VariableObject(varDecMatch.group(3), varDecMatch.group(2)));
			}
		}
	}

	private static void variableDeclareLine(Scope scope, String line) throws VariableException {
		// get the type (make sure legal), for each sections between the comma's, check if assignment,
		// make varObject and try to add it.
		boolean isFinal = false;
		Matcher variableDeclarationMatch = RegexDepot.VARIABLE_DECLARATION_PATTERN.matcher(line);
		variableDeclarationMatch.find();
		String type = variableDeclarationMatch.group(2);
		if (variableDeclarationMatch.group(1) != null) { // if grp 1 exists it's final, else null
			isFinal = true;
		}

		// split varNames and values by commas and keep without final&type
		String[] namesAndAssignment = variableDeclarationMatch.group(3).split(",");
		for (String var : namesAndAssignment) {

			Matcher assignment = RegexDepot.VARIABLE_ASSIGNEMENT_PATTERN.matcher(var);
			Matcher varWithoutAssignment = RegexDepot.VARIABLE_PATTERN.matcher(var);

			if (assignment.find()) {
				scope.addVar(new VariableObject(assignment.group(1), type));
				variableAssignLine(scope, line, assignment);
			} else if (varWithoutAssignment.find()) {
				if (isFinal) {
					throw new IllegalAssignmentException(var);
				}
				String name = varWithoutAssignment.group(0);
				VariableObject newVar = new VariableObject(name, type);

				scope.addVar(newVar);

			} else {
				// throw new not valid declaration.
			}
		}
	}

	private static void variableAssignLine(Scope scope, String line, Matcher assignment) throws
			IllegalAssignmentException {
		String varName = assignment.group(1);
		String value = assignment.group(2);
		Matcher varValueName = RegexDepot.VARIABLE_PATTERN.matcher(value);

		VariableObject varToAssign = scope.contains(varName);
		VariableObject valueVar = scope.contains(value);

		if (varToAssign == null) {
			throw new IllegalAssignmentException(varName);
		}

		if (!valueVar.getType().equals(varToAssign.getType())) {
			throw new IllegalAssignmentException(varName);
		}


	}


	private static void methodCallChecker(Scope scope, Matcher lineMatcher) throws invalidMethodException,
			VariableException {


		String methodName = lineMatcher.group(1);
		String[] arguments = lineMatcher.group(2).split(ARG_DELIM);


		Method methodMatched = null;
		Scope tempScope = scope;
		while (tempScope != null) {
			for (Method temp : tempScope.getAllMethods()) {
				if (temp.getName().equals(methodName)) {
					methodMatched = temp;
					continue;
				}
			}
			tempScope = tempScope.getParent();

		}

		if (methodMatched == null) {
			throw new invalidMethodException(methodName);
		}
		methodArgParse(methodMatched);

		ArrayList<VariableObject> methodArguments = methodMatched.getArguments();


		for (int i = 0; i < arguments.length; i++) {
			VariableObject checkedVarName = scope.contains(arguments[i]);

			if (checkedVarName != null) {
				methodArguments.get(i).setValue(checkedVarName.getValue());

			} else {
				methodArguments.get(i).setValue(arguments[i]);
			}
		}

	}

	/**
	 * Checks if the given conditions are legal conditions.
	 *
	 * @param scope         the scope composing the variables.
	 * @param conditionLine the string of conditions.
	 * @return true if conditions given are legal, else false.
	 */
	private static boolean conditionsChecker(Scope scope, String conditionLine) {
		String[] arguments = conditionLine.split(CONDITION_DELIM);

		for (String arg : arguments) {
			if (arg.matches(BOOLEAN.getPattern())) { // arg is boolean string
				continue;
			} else if (arg.matches(RegexDepot.VARIABLE_NAME)) { // arg is variableObject
				if (scope.isVarValueInitialized(arg)) {
					continue;
				}
			} else return false;
		}
		return true;
	}


}
