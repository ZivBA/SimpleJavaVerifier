package dataStructures.scope;

import dataStructures.scope.exceptions.InvalidReturnException;
import dataStructures.scope.exceptions.ScopeException;
import dataStructures.vars.exceptions.DuplicateAssignmentException;
import dataStructures.vars.VariableObject;
import dataStructures.vars.exceptions.VariableException;
import parsing.RegexDepot;

import java.util.ArrayList;
import java.util.regex.Matcher;

/**
 * the Method object is an extension of the scope. it is mainly created to override some Scope speciifc
 * methods with Method relevant ones.
 */
public class Method extends Scope {
	// constants
	private static final int LINE_BEFORE_LAST = 2;

	// data members
	private String name = null;
	private String argString = ""; 	// textual representation of the method arguments.
	private final ArrayList<VariableObject> argList = new ArrayList<>(); // a list of arguments as VarObj.

	/**
	 * the constructor for a Method is similar to the scope one. it parses the Method parameters, checks if
	 * the method has a valid return line and then iterates over the lines to recursively build new scopes
	 * @param sourceFile - the source block of this method
	 * @param parent	 - the parent Scope of this method
	 * @throws ScopeException
	 */
	public Method(ArrayList<String> sourceFile, Scope parent) throws ScopeException {
		this.sourceFile = sourceFile;
		this.parent = parent;
		this.type = METHOD_TYPE;

		checkReturnEndsMethod();
		parseParams();
		recurScopeBuilder();
	}

	/**
	 * Checks that the last line before the scope closer "}" is a legal return keyword
	 *
	 * @throws ScopeException if invalid return in line
	 */
	private void checkReturnEndsMethod() throws ScopeException {
		int returnLineIndex = sourceFile.size() - LINE_BEFORE_LAST;
		String returnLine = sourceFile.get(returnLineIndex);
		if (!returnLine.matches(RegexDepot.VALID_LINES)) {
			throw new InvalidReturnException();
		}
	}

	/**
	 * parses the Method decleration. sets the argString data member with the method arguments, sets the
	 * method name and removes the decleration line from the source block.
	 */
	private void parseParams() {

		String firstLine = sourceFile.get(0);
		Matcher methodMatch = RegexDepot.METHOD_PATTERN.matcher(firstLine);
		methodMatch.find();
		name = methodMatch.group(1);
		argString = methodMatch.group(2);
		sourceFile.remove(0);
	}


	/**
	 * returns the method name
	 * @return String method name;
	 */
	public String getName() {
		return name;
	}

	/**
	 * returns the arguments string
	 * @return String argString
	 */
	public String getArgString() {
		return argString;
	}

	/**
	 * when relevant, this method is used to add a processed argument into the ArgList array. it gets the
	 * argument as a VariableObject and if it was not already added to the list, adds it.
	 * @param argument	- the VarObj to add
	 * @return			- returns the reference to the argument in the argList
	 * @throws DuplicateAssignmentException
	 */
	public boolean addArgumentToArgList(VariableObject argument) throws DuplicateAssignmentException {
		if (argList.contains(argument)) {
			throw new DuplicateAssignmentException(argument);
		}
		return argList.add(argument);

	}

	/**
	 * returns the argument list
	 * @return ArrayList of arguments as VarObj
	 */
	public ArrayList<VariableObject> getArguments() {
		return argList;
	}

	/**
	 * override for the Scope "contains" method, this is used to introduce global variables to the method
	 * scope. when a Method needs to evaluate or assign to a global scope, it cannot do so directly, as
	 * this introduces issues when parsing other scopes or methods who shuldnt be aware of changes
	 * performed during a method call.
	 * @param name    the name of the variable to find
	 * @return			the Variable Object found or null.
	 */
	public VariableObject contains(String name) throws VariableException {
		VariableObject localVariable = varStore.getVar(name);
		if (localVariable == null) {
			VariableObject temp = parent.contains(name);
			if (temp != null) {
				VariableObject varFromParent = new VariableObject(temp.getName(), temp.getType(), temp
						.getValue(), temp.isFinal());
				varStore.addVar(varFromParent);
				return varFromParent;
			}
		}
		return localVariable;
	}

}

