package dataStructures.scope;

import dataStructures.vars.VariableObject;
import dataStructures.vars.VariableStorage;
import parsing.RegexDepot;
import parsing.exceptions.DuplicateAssignmentException;
import parsing.exceptions.InvalidScopeException;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;

/**
 * Created by rooty on 6/11/15.
 */
public class Scope {

	// Data Members
	private final VariableStorage varStore = new VariableStorage();
	private ArrayList<String> sourceFile;
	private Scope parent;
	private ArrayList<Scope> children;
	private String type;
	private String conditions;

	public Scope(Scanner sourceFile, Scope parent) throws InvalidScopeException {
		// TODO Check for legal types (void method, if, while)
		this.sourceFile = stringToArray(sourceFile);

		checkType();
		this.parent = parent;
		addMembers();

	}

	private ArrayList<String> stringToArray(Scanner sourceFile) {
		ArrayList<String> tempArr = new ArrayList<>();
		while (sourceFile.hasNext()) {
			tempArr.add(sourceFile.nextLine());
		}
		return tempArr;
	}

	private void addMembers() {

		for (String line : sourceFile) {

			Matcher varMatcher = RegexDepot.VARIABLE_DECLARATION_PATTERN.matcher(line);
			Matcher conditionMatch = RegexDepot.CONDITION_PATTERN.matcher(line);
			Matcher methodMatch = RegexDepot.METHOD_PATTERN.matcher(line);








		}


	}

	private void checkType() throws InvalidScopeException {

		String firstLine = sourceFile.get(0);
		String firstWord = firstLine.substring(0, firstLine.indexOf(" "));

		Matcher conditionMatch = RegexDepot.CONDITION_PATTERN.matcher(firstLine);
		Matcher methodMatch = RegexDepot.METHOD_PATTERN.matcher(firstLine);

		// is if|while?
		if (conditionMatch.find()) {
			type = conditionMatch.group(1);
			conditions = conditionMatch.group(2);
			sourceFile.remove(0);
		}
		// is method?
		else if (methodMatch.find()) {
			type = methodMatch.group(1);
			conditions = methodMatch.group(2);
			sourceFile.remove(0);
		}
		// is else?
		else if (parent != null) {
			throw new InvalidScopeException(firstLine);
		}
	}

	/**
	 * simple contain check for the Scope level - does it contain a variable "name".
	 *
	 * @param name
	 * @return
	 */
	public VariableObject contains(String name) {
		return varStore.getVar(name);
	}

	/**
	 * try to add variable to the scope storage. if it exists - throw exception. otherwise return the
	 * pointer to the added var in the storage.
	 *
	 * @param var
	 * @return
	 * @throws DuplicateAssignmentException
	 */
	public VariableObject addVar(VariableObject var) throws DuplicateAssignmentException {

		return varStore.addVar(var);

	}

	public enum Types { // TODO get enums down, maybe get the name from the parser?
		METHOD,
		WHILE,
		IF


	}

}

