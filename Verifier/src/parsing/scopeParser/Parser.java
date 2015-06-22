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
import parsing.exceptions.*;

import java.util.ArrayList;
import java.util.regex.Matcher;

import static dataStructures.vars.VariableObject.VarTypeAndValue.BOOLEAN;

/**
 * Static class that parses the given file.
 * Given file must be defined as a scope that composes the lines of code to parse.
 */
public class Parser {
	//Constants
	private static final boolean DEFAULT_FINAL = false;

	// Static Methods

	/**
	 * Main static method that manages the parsing of the sjava file given.
	 * Starts parsing from the main scope and goes over each line in the file given. Methods parsed last(**)
	 * When meeting a conditional scope line, checks that the condition is valid and recursively start
	 * parsing from that scope onto its last nested scope (leaf of the tree).
	 * (**) When meeting a method call line, parse that specific method and then check the calling validity.
	 * When all lines have been parsed (excluding method scopes), start parsing the method scopes in order.
	 * Method scopes wont be parsed twice (see methodCallChecker).
	 *
	 * @param scope the given scope to parse.
	 * @throws VariableException
	 * @throws ScopeException
	 * @throws ParsingException
	 */
	public static void startParsing(Scope scope) throws VariableException, ScopeException, ParsingException {
		ArrayList<String> fileLines = scope.getSrc();
		// Parse each line in given scope.
		for (String line : fileLines) {
			Matcher variableDeclarationMatch = RegexDepot.VARIABLE_DECLARATION_PATTERN.matcher(line);
			Matcher variableAssignMatch = RegexDepot.VARIABLE_ASSIGNEMENT_PATTERN.matcher(line);
			Matcher conditionScopeMatch = RegexDepot.CONDITION_PATTERN.matcher(line);
			Matcher methodCallMatch = RegexDepot.METHOD_CALL_PATTERN.matcher(line);
			// Matching cases
			if (variableDeclarationMatch.matches()) {
				variableDeclareLine(scope, line);
			} else if (variableAssignMatch.matches()) {
				variableAssignLine(scope, DEFAULT_FINAL, variableAssignMatch);
			} else if (methodCallMatch.find()) {
				methodCallChecker(scope, methodCallMatch);
			} else if (conditionScopeMatch.matches()) {
				if (!conditionsChecker(scope, conditionScopeMatch.group(2))) {
					throw new InvalidConditionsException();
				}
				startParsing(scope.getAllChildren().pollFirst()); // parse nested conditional scope
			} else if (line.matches(RegexDepot.VALID_LINES)) {
				// pass;
			} else {
				throw new SyntaxException();
			}
		} // finished running over all lines but the method lines
		for (Method temp : scope.getAllMethods()) {
			methodArgParse(temp);
			startParsing(temp);
		}
	}

	/**
	 * Parses the arguments from the methods signature.
	 * Checks that each argument is of valid form and adds it as VariableObjects to the methods arguments
	 * list so it could be compared to the arguments given from the method call.
	 *
	 * @param method the method scope to get the arguments from.
	 * @throws VariableException
	 * @throws SyntaxException
	 */
	private static void methodArgParse(Method method) throws VariableException, SyntaxException {
		if (!method.getArguments().isEmpty()) {
			return; // no arguments to parse
		}
		String[] arguments = method.getArgString().split(RegexDepot.ARG_DELIM);
		for (String argument : arguments) {
			if (argument.length() != 0) { // wont parse empty string
				Matcher varDecMatch = RegexDepot.VARIABLE_DECLARATION_PATTERN.matcher(argument.trim());
				if (!varDecMatch.matches()) {
					throw new IllegalAssignmentException(argument);// Argument isnt of legal declare type
				}
				String argumentName = varDecMatch.group(3).trim();
				String argumentType = varDecMatch.group(2).trim();

				if (method.addArgumentToArgList(new VariableObject(argumentName, argumentType))) {
					variableDeclareLine(method, argument);
				}
			}
		}
	}

	/**
	 * Attempts to create a variable (declaration of variable has explicit type).
	 * Splits the given line into different variable names and assignments (if only one, stays that way)
	 * and initializes them if their types are legal and if they dont exist already.
	 * For the initialization itself, there are two cases:
	 * 1) Variable has value to assign (calls variableAssignLine in order to give value and final status)
	 * 2) Variable has no value, thus is only declared (cannot be final).
	 *
	 * @param scope the given scope that composes the variable to create.
	 * @param line  the given line that represents the variable declaration in the code.
	 * @throws VariableException
	 * @throws SyntaxException
	 */
	private static void variableDeclareLine(Scope scope, String line) throws VariableException, SyntaxException {
		boolean isFinal = DEFAULT_FINAL; // false
		Matcher variableDeclarationMatch = RegexDepot.VARIABLE_DECLARATION_PATTERN.matcher(line);
		variableDeclarationMatch.find(); // makes sure the declaration exists
		String type = variableDeclarationMatch.group(2).trim(); // type for all assignments in line
		if (variableDeclarationMatch.group(1) != null) { // if group 1 exists it's final, else null
			isFinal = true;
		}
		// split varNames and values by commas and keep without final&type
		String[] namesAndAssignment = variableDeclarationMatch.group(3).split(",");
		for (String var : namesAndAssignment) {
			Matcher assignment = RegexDepot.VARIABLE_ASSIGNEMENT_PATTERN.matcher(var.trim());// has value
			Matcher varWithoutAssignment = RegexDepot.VARIABLE_PATTERN.matcher(var.trim()); // has no value

			if (assignment.matches()) { // case 1
				scope.addVar(new VariableObject(assignment.group(1).trim(), type)); // create value w/o val
				variableAssignLine(scope, isFinal, assignment); // give value to the variable
			} else if (varWithoutAssignment.matches()) { // case 2
				if (isFinal) { // cannot assign final variable w/o value
					throw new IllegalAssignmentException(var);
				}
				String name = varWithoutAssignment.group(0).trim();
				scope.addVar(new VariableObject(name, type));
			} else { // no case matched, line has illegal syntax
				throw new SyntaxException();
			}
		}
	}

	/**
	 * Gives an existing variable a new value.
	 * Value will be updated iff the variable exists and isnt final, and if the type matches.
	 * The new value to be assigned could be a value that came from another initialized variable, thus
	 * called referenceFromVar.
	 *
	 * @param scope      the scope that composes the variable to assign.
	 * @param isFinal    declares if the variable should be final.
	 * @param assignment the mathcer object that represents the line of assignment from code.
	 * @throws VariableException
	 */
	private static void variableAssignLine(Scope scope, boolean isFinal, Matcher assignment) throws
			VariableException {

		String varNameToAssignValue = assignment.group(1);
		String newValue = assignment.group(2);
		VariableObject existingVariable = scope.contains(varNameToAssignValue);
		VariableObject referenceFromVar = scope.contains(newValue);

		if (existingVariable == null || existingVariable.isFinal()) {
			throw new IllegalAssignmentException(varNameToAssignValue);
		}
		if (referenceFromVar != null) {
			if (!referenceFromVar.getType().equals(existingVariable.getType())) {
				//Values cannot reference each other, different types.
				throw new IllegalAssignmentException(varNameToAssignValue);
			} else {
				if (referenceFromVar.getValue() == null) {
					// if the value is not initialized - check if it's a MethodArg var. if casting fails
					// or if the var is not in args - throw exception. else skip value check by return;
					try {
						if (((Method) scope).getArguments().contains(referenceFromVar)) return;
					} catch (ClassCastException e) {
						// we expect class cast exception, and continue on to throw assign exception.
					}
					throw new IllegalAssignmentException(varNameToAssignValue);
				}
				return;
			}
		}
		// use setValue's type checking to ensure valid assignment.
		existingVariable.setValue(newValue);
		if (isFinal) { // sets the variable as final after value assignment.
			existingVariable.setFinal(isFinal);
		}
	}

	/**
	 * Checks that the given line that calls a method, attempts to call an existing method.
	 * If the method exists, parse it so it could be compared and call the auxiliary method
	 * compareArguments.
	 * @param scope       the scope composing the calling of the method.
	 * @param lineMatcher the Matcher object representing the call line of the method.
	 * @throws invalidMethodException
	 * @throws VariableException
	 * @throws ParsingException
	 */
	private static void methodCallChecker(Scope scope, Matcher lineMatcher) throws ScopeException,
			VariableException, ParsingException {

		String methodName = lineMatcher.group(1);
		String[] methodCallArguments = new String[0];
		if (!lineMatcher.group(2).isEmpty()) { // no arguments given to method call
			methodCallArguments = lineMatcher.group(2).trim().split(RegexDepot.ARG_DELIM);
		}
		Method methodMatched = null;
		Scope tempScope = scope;
		// search for the method that should be called.
		while (tempScope != null) { // recursively go through method lists up to root scope.
			for (Method temp : tempScope.getAllMethods()) {
				if (temp.getName().equals(methodName)) {
					methodMatched = temp;
					break;
				}
			}
			tempScope = tempScope.getParent();
		}
		if (methodMatched == null) { // no such method exists
			throw new invalidMethodException(methodName);
		}
		// parse the method so it could be compared to the arguments in calling of the method
		methodArgParse(methodMatched);
		scope.getAllMethods().remove(methodMatched); // makes sure we dont parse method twice
		ArrayList<VariableObject> methodArguments = methodMatched.getArguments();
		compareArguments(methodArguments, methodCallArguments, scope);
	}

	/**
	 * Compares between the arguments in the methods signature and the arguments given in the attempted
	 * call to the method.
	 * If the arguments dont match, then a parsing exception will be thrown.
	 * @param methodArguments the arguments in the methods signature.
	 * @param methodCallArguments the arguments given in the attempted call of the method.
	 * @param scope the scope composing the method call line
	 * @throws ParsingException
	 * @throws VariableException
	 */
	private static void compareArguments(ArrayList<VariableObject> methodArguments,
										 String[] methodCallArguments, Scope scope)
			throws ParsingException, VariableException {

		if (methodArguments.size() != methodCallArguments.length) {
			throw new InvalidNumOfArguments(); // unequal number of arguments
		}
		for (int i = 0; i < methodCallArguments.length; i++) {
			VariableObject checkedVarName = scope.contains(methodCallArguments[i]);
			if (checkedVarName != null) { // variable as argument exists, check legal assignment
				methodArguments.get(i).setValue(checkedVarName.getValue());
			} else { // check assignment for non-existing variable, literal assignment
				methodArguments.get(i).setValue(methodCallArguments[i]);
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
	private static boolean conditionsChecker(Scope scope, String conditionLine) throws VariableException {
		String[] arguments = conditionLine.split(RegexDepot.CONDITION_DELIM);

		for (String arg : arguments) {
			if (arg.matches(BOOLEAN.getPattern())) { // arg is boolean string
				continue;
			} else if (arg.matches(RegexDepot.VARIABLE_NAME)) { // arg is variableObject
				VariableObject argumentObj = scope.contains(arg.trim());
				if (scope.isVarValueInitialized(arg.trim()) && argumentObj.getType().matches(RegexDepot
						.VALID_BOOL_TYPES)) {
					continue;
				}
			}
			return false;
		}
		return true;
	}

}
