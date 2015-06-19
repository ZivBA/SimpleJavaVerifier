package dataStructures.scope;

import dataStructures.vars.VariableObject;
import dataStructures.vars.VariableStorage;
import parsing.RegexDepot;
import parsing.exceptions.DuplicateAssignmentException;
import parsing.exceptions.InvalidScopeException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rooty on 6/11/15.
 */
public class Scope {

	// Data Members
	private final VariableStorage varStore = new VariableStorage();
	private ArrayList<String> sourceFile;
	private Scope parent = null;
	private ArrayList<Scope> children = new ArrayList<>();
	private String type = null;
	private String conditions = null;

	public Scope(Scanner sourceFile, Scope parent) throws InvalidScopeException {
		// TODO Check for legal types (void method, if, while)
		this.sourceFile = stringToArray(sourceFile);
		this.parent = parent;
		scopeInit();

	}
	public Scope(ArrayList<String> sourceFile, Scope parent) throws InvalidScopeException {
		this.sourceFile = sourceFile;
		this.parent = parent;
		scopeInit();
	}

	private void scopeInit() throws InvalidScopeException {
		checkType();
		recurScopeBuilder();
	}

	private ArrayList<String> stringToArray(Scanner sourceFile) {
		ArrayList<String> tempArr = new ArrayList<>();
		while (sourceFile.hasNext()) {
			tempArr.add(sourceFile.nextLine());
		}
		return tempArr;
	}

	private void recurScopeBuilder() throws InvalidScopeException {

		Pattern p = Pattern.compile("\\{");
		Pattern p2 = Pattern.compile("\\}");

		Iterator<String> sourceIterator = sourceFile.iterator();
		while (sourceIterator.hasNext()) {
			String line = sourceIterator.next();

			Matcher openBrack = p.matcher(line);
			Matcher closeBrack = p2.matcher(line);

			if (openBrack.find()){
				ArrayList<String> tempArray = new ArrayList<>();
				int bracketCounter = 1;
				while (bracketCounter != 0){

					tempArray.add(sourceFile.remove(sourceFile.indexOf(line)));
					line = sourceIterator.next();
					openBrack.reset(line);
					closeBrack.reset(line);

					if (openBrack.find()){
						bracketCounter++;
					}else if (closeBrack.find()){
						bracketCounter--;
					}
				}

				children.add(new Scope(tempArray,this));
			}
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

}

