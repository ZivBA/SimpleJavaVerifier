package dataStructures.scope;

import dataStructures.scope.exceptions.InvalidReturnException;
import dataStructures.scope.exceptions.ScopeException;
import dataStructures.vars.exceptions.DuplicateAssignmentException;
import dataStructures.vars.VariableObject;
import parsing.RegexDepot;

import java.util.ArrayList;
import java.util.regex.Matcher;

/**
 * Created by rooty on 6/21/15.
 */
public class Method extends Scope {
	private static final int LINE_BEFORE_LAST = 2;
	private static final String TYPE = "Method";
	private String name = null;
	private String argString = "";
	private ArrayList<VariableObject> argList = new ArrayList<>();

	public Method(ArrayList<String> sourceFile, Scope parent) throws ScopeException {
		this.sourceFile = sourceFile;
		this.parent = parent;
		this.type = TYPE;

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
		if (!returnLine.matches(RegexDepot.RETURN_PATTERN)) {
			throw new InvalidReturnException();
		}
	}

	private void parseParams() throws ScopeException {

		String firstLine = sourceFile.get(0);
		Matcher methodMatch = RegexDepot.METHOD_PATTERN.matcher(firstLine);
		methodMatch.find();
		name = methodMatch.group(1);
		argString = methodMatch.group(2);
		sourceFile.remove(0);
	}


	public String getName() {
		return name;
	}


	public String getArgString() {
		return argString;
	}

	public boolean addMethodArgument(VariableObject argument) throws DuplicateAssignmentException {
		if (argList.contains(argument)) {
			throw new DuplicateAssignmentException(argument);
		}
		return argList.add(argument);

	}

	public ArrayList<VariableObject> getArguments() {
		return argList;
	}


}
