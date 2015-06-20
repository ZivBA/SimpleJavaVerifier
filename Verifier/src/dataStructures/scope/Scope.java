package dataStructures.scope;

import dataStructures.vars.VariableObject;
import dataStructures.vars.VariableStorage;
import parsing.RegexDepot;
import parsing.exceptions.DuplicateAssignmentException;
import parsing.exceptions.InvalidScopeException;
import sun.applet.Main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rooty on 6/11/15.
 */
public class Scope {

	private static final String MAINSCOPE = "Main";
	// Data Members
	private final VariableStorage varStore = new VariableStorage();
	private ArrayList<String> sourceFile;
	private Scope parent = null;
	private LinkedList<Scope> children = new LinkedList<>();
	private LinkedList<Scope> methods = new LinkedList<>();

	private String type = null;
	private String conditions = null;


	public Scope(ArrayList<String> sourceFile, Scope parent) throws InvalidScopeException {
		this.sourceFile = sourceFile;
		this.parent = parent;
		scopeInit();
	}

	private void scopeInit() throws InvalidScopeException {
		checkType();
		recurScopeBuilder();
	}

	private void recurScopeBuilder() throws InvalidScopeException {

		//TODO fix infinite recursion with firstline being a method
		Pattern p = Pattern.compile("\\{");
		Pattern p2 = Pattern.compile("\\}");

		Iterator<String> sourceIterator = sourceFile.iterator();
		while (sourceIterator.hasNext()) {
			String line = sourceIterator.next();

			Matcher openBrack = p.matcher(line);
			Matcher closeBrack = p2.matcher(line);

			if (openBrack.find()){
				String firstLine = line;
				ArrayList<String> tempArray = new ArrayList<>();
				int bracketCounter = 1;
				while (bracketCounter != 0){

					tempArray.add(line);
					sourceIterator.remove();
					line = sourceIterator.next();
					openBrack.reset(line);
					closeBrack.reset(line);

					if (openBrack.find()){
						bracketCounter++;
					}else if (closeBrack.find()){
						bracketCounter--;
					}
				}
				//add final row to temparray
				tempArray.add(line);

				//get the position of the last line so marker could be inserted there
				int positionForMarker = sourceFile.indexOf(line);

				//remove last inserted row
				sourceIterator.remove();

				//add commented marker for method or condition clause
				sourceFile.add(positionForMarker,"//"+firstLine.substring(0,line.length()-1));
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
			type = firstWord;
			conditions = conditionMatch.group(2);
			sourceFile.remove(0);
			parent.children.addLast(this);
		}
		// is method?
		else if (methodMatch.find()) {
			type = firstWord;
			conditions = methodMatch.group(2);
			sourceFile.remove(0);
			parent.methods.addLast(this);
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

	public ArrayList<String> getSrc() {
		return sourceFile;
	}

	public Scope getChild(int index) {
		return children.get(index);
	}
}

