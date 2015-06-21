package dataStructures.scope;

import dataStructures.scope.exceptions.*;
import dataStructures.vars.VariableObject;
import dataStructures.vars.VariableStorage;
import dataStructures.vars.exceptions.*;
import parsing.RegexDepot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rooty on 6/11/15.
 */
public class Scope {

	private static final String MAINSCOPE = "Main";
	// Data Members
	protected final VariableStorage varStore = new VariableStorage();
	protected ArrayList<String> sourceFile = new ArrayList<>();
	protected Scope parent = null;
	protected LinkedList<Scope> children = new LinkedList<>();
	private LinkedList<Method> methods = new LinkedList<>();

	protected String type = null;
	private String conditions = null;


	public Scope(ArrayList<String> sourceFile, Scope parent) throws ScopeException {
		this.sourceFile = sourceFile;
		this.parent = parent;

		//padd the main scope with spaces to accomodate for scopes that start with a void/if/while
		if (parent == null) {
			sourceFile.add(0, " ");
			sourceFile.add(sourceFile.size(), " ");
			type = MAINSCOPE;
			recurScopeBuilder();

		} else {
			parseParams();
			recurScopeBuilder();
		}
	}

	public Scope() {
	}

	private void parseParams() throws InvalidScopeException {

		String firstLine = sourceFile.get(0);
		Matcher conditionMatch = RegexDepot.CONDITION_PATTERN.matcher(firstLine);
		conditionMatch.find();
		type = conditionMatch.group(1);
		conditions = conditionMatch.group(2);
		sourceFile.remove(0);

	}

	protected void recurScopeBuilder() throws ScopeException {

		Pattern p = Pattern.compile("\\{");
		Pattern p2 = Pattern.compile("\\}");
		ListIterator<String> sourceIterator = sourceFile.listIterator();
		while (sourceIterator.hasNext()) {
			String line = sourceIterator.next();

			Matcher openBrack = p.matcher(line);
			Matcher closeBrack = p2.matcher(line);

			if (openBrack.find()) {
				String firstLine = line;
				ArrayList<String> tempArray = new ArrayList<>();
				int bracketCounter = 1;
				while (bracketCounter != 0) {

					tempArray.add(line);
					sourceIterator.remove();
					line = sourceIterator.next();
					openBrack.reset(line);
					closeBrack.reset(line);

					if (openBrack.find()) {
						bracketCounter++;
					} else if (closeBrack.find()) {
						bracketCounter--;
					}
				}
				//add final row to temparray
				tempArray.add(line);

				//add commented marker for method or condition clause
				sourceIterator.set(firstLine.substring(0, firstLine.indexOf('{')));
				createChild(tempArray, this);


			}
		}


	}

	protected void createChild(ArrayList<String> sourceBlock, Scope parent) throws ScopeException {
		String firstLine = sourceBlock.get(0);

		Matcher conditionMatch = RegexDepot.CONDITION_PATTERN.matcher(firstLine);
		Matcher methodMatch = RegexDepot.METHOD_PATTERN.matcher(firstLine);

		if (methodMatch.matches()) {
			methods.addLast(new Method(sourceBlock, this));
		} else if (conditionMatch.matches() && parent!=null) {
			children.addLast(new Scope(sourceBlock, this));
		} else {
			throw new InvalidScopeException(firstLine);
		}
	}


	/**
	 * simple contain check for the Scope level - does it contain a variable "name".
	 * <p/>
	 * nope! elaborate contain check that recurses through parents.
	 *
	 * @param name
	 * @return
	 */
	public VariableObject contains(String name) {
		VariableObject temp = varStore.getVar(name);
		if (temp != null) {
			return temp;
		}
		if (parent != null) {
			return parent.contains(name);
		}
		return null;
	}

	/**
	 * Checks if the given name is an initialized variable and if it has an assigned value.
	 * @param name the name of the variableObject to check.
	 * @return true if variable is initialized and has a value, else false.
	 */
	public boolean isVarValueInitialized(String name) {
		VariableObject variable = contains(name);
		if (variable != null){
			if (variable.getValue() != null){
				return true;
			}
		}
		return false;
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

	public LinkedList<Scope> getAllChildren() {
		return children;
	}

	public LinkedList<Method> getAllMethods() {
		return methods;
	}


	public String toString() {
		return "Type: " + type + " conditions: " + conditions;
	}

	/**
	 * Gets parent.
	 *
	 * @return Value of parent.
	 */
	public Scope getParent() {
		return parent;
	}


	/**
	 * Gets conditions.
	 *
	 * @return Value of conditions.
	 */
	public String getConditions() {
		return conditions;
	}

}

