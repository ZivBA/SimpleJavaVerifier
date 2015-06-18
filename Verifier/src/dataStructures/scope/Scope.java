package dataStructures.scope;

import dataStructures.vars.VariableObject;
import dataStructures.vars.VariableStorage;
import parsing.RegexDepot;
import parsing.exceptions.DuplicateAssignmentException;
import parsing.exceptions.InvalidScopeException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;

/**
 * Created by rooty on 6/11/15.
 */
public class Scope {

	// Data Members
	private final VariableStorage varStore = new VariableStorage();
	private final Scanner sourceFile;
	private Scope parent;
	private ArrayList<Scope> children;
	private String type;
	private String conditions;

	public Scope(Scanner sourceFile, Scope parent) throws InvalidScopeException {
		// TODO Check for legal types (void method, if, while)
		this.sourceFile = sourceFile;
		this.type = checkType();
		this.parent = parent;

	}

	private void addMembers() {

	}

	private String checkType() throws InvalidScopeException {
		String firstLine = sourceFile.nextLine();
		Matcher scopeStart = RegexDepot.SCOPE_PATTERN.matcher(firstLine);

		if (!scopeStart.matches()) {
			throw new parsing.exceptions.InvalidScopeException(firstLine);
		}

		return scopeStart.group();

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

