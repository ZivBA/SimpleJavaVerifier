package dataStructures.scope;
// all relevant exceptions for the Scope object
import dataStructures.scope.exceptions.InvalidScopeException;
import dataStructures.scope.exceptions.ScopeException;
import dataStructures.vars.exceptions.DuplicateAssignmentException;
import dataStructures.vars.exceptions.VariableException;

//custom data structures in use by the Scope
import dataStructures.vars.VariableObject;
import dataStructures.vars.VariableStorage;

// regex expressions storage - used by all
import parsing.RegexDepot;

// java utils used by the class
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Scope class is the main object used by the validator program. </br>
 * a Scope is a "node" in the tree which describes the sjava file analyzed. it contains the various data
 * members held by a scope, such as variables, the source code for the scope, the type of the scope
 * (if/while/Main) and conditions if applicable. </br>
 * a Scope needs a sourcefile block and a parent to be created.
 */
public class Scope {

	// Constants used by the class.
	private static final String MAINSCOPE = "Main";
	protected static final String METHOD_TYPE = "Method";

	// Data Members
	protected final VariableStorage varStore = new VariableStorage();
	protected ArrayList<String> sourceFile = new ArrayList<>();
	protected Scope parent = null;
	private final LinkedList<Scope> children = new LinkedList<>();
	protected String type = null;
	private final LinkedList<Method> methods = new LinkedList<>();

	/**
	 * Recursive constructor - gets a bock of source code and a parent reference. iterates over the source
	 * code and creates nested Scopes if they exist.
	 * @param sourceFile - an ArrayList of Strings (lines) representing the code for this scope.
	 * @param parent	 - a Scope object representing the parent of this scope
	 * @throws ScopeException - parent class for all types of exceptions caught during Scope processing.
	 */
	public Scope(ArrayList<String> sourceFile, Scope parent) throws ScopeException {
		this.sourceFile = sourceFile;
		this.parent = parent;

		// the recursive builder takes into accout that the first line of a scope has arguments or
		// conditions. in order to accomodate for the Main scope, temporary padding is added to the source
		// code and removed after processing.
		if (parent == null) { 							// if this is the Main scope
			sourceFile.add(0, " ");						// padd first line
			sourceFile.add(sourceFile.size(), " ");		// padd last line
			type = MAINSCOPE;							// set Main
			recurScopeBuilder();						// process scope
			sourceFile.remove(0);						// remove padding
			sourceFile.remove(sourceFile.size()-1);


		} else {	// if not the main method, process normally.
			parseParams();
			recurScopeBuilder();

		}
	}

	/**
	 * Empty constructor for the extending Method class
	 */
	public Scope() {
	}

	/**
	 * process the scope decleration, finds the conditions and arguments, sets the type of the scope and
	 * removes the decleration from the source file being processed.
	 */
	private void parseParams() {

		String firstLine = sourceFile.get(0);
		Matcher conditionMatch = RegexDepot.CONDITION_PATTERN.matcher(firstLine);
		conditionMatch.find();
		type = conditionMatch.group(1);
		sourceFile.remove(0);

	}

	/**
	 * the main part of the Scope constructor - this method looks for matching curly brackets, cuts out the
	 * code in between and creates a new nested Scope within the current one.
	 * @throws ScopeException
	 */
	protected void recurScopeBuilder() throws ScopeException {

		Pattern p = Pattern.compile("\\{");
		Pattern p2 = Pattern.compile("\\}");
		ListIterator<String> sourceIterator = sourceFile.listIterator();

		// iterate over all lines in the current scope source code.
		while (sourceIterator.hasNext()) {
			String line = sourceIterator.next();

			Matcher openBrack = p.matcher(line);
			Matcher closeBrack = p2.matcher(line);

			// find nested scope start designated by an opening curly brace {
			if (openBrack.find()) {
				String firstLine = line;
				ArrayList<String> tempArray = new ArrayList<>();
				int bracketCounter = 1;
				while (bracketCounter != 0) {

					// add all lines until the matching closing brace to the temporary array
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

				//once the last brace is found, add to the temporary array
				tempArray.add(line);

				//replace the final line with the first line of the nested scope. remove the final brace to
				// prevent infinite loops in the recursive builder.
				sourceIterator.set(firstLine.substring(0, firstLine.indexOf('{')));
				createChild(tempArray, this); // create the nested scope with current scope as parent.


			}
		}


	}

	/**
	 * identifies the type of scope contained in the source block, and creates the appropriate nested scope.
	 * @param sourceBlock - the source code block for the new scope object
	 * @param parent - the scope containing this code within it.
	 * @throws ScopeException
	 */
	void createChild(ArrayList<String> sourceBlock, Scope parent) throws ScopeException {

		String firstLine = sourceBlock.get(0);

		Matcher conditionMatch = RegexDepot.CONDITION_PATTERN.matcher(firstLine);
		Matcher methodMatch = RegexDepot.METHOD_PATTERN.matcher(firstLine);

		// if it's a method, create a new Method object and add to current scope's methods storage
		// if it's a conditional scope, create and add appropriatly. if none match then there's some
		// uncaught syntax error with the scope - throw exception with the culprit line.
		if (methodMatch.matches()) {
			methods.addLast(new Method(sourceBlock, this));
		} else if (conditionMatch.matches() && parent != null) {
			children.addLast(new Scope(sourceBlock, this));
		} else {
			throw new InvalidScopeException(firstLine);
		}
	}


	/**
	 * checks if a variable of the supplied name exists within the this scope's variable storage. if not,
	 * recurse through the parent scopes to find it higher up. finally return null if not found.
	 *
	 * @param name    the name of the variable to find
	 * @return			the Variable Object found or null.
	 */
	public VariableObject contains(String name) throws VariableException {
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
	 *
	 * @param name the name of the variableObject to check.
	 * @return true if variable is initialized and has a value, else false.
	 */
	public boolean isVarValueInitialized(String name) throws VariableException {
		VariableObject variable = contains(name);
		if (variable != null) {
			if (variable.getValue() != null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * try to add variable to the scope storage. if a variable by the same neam exists - throw exception.
	 * otherwise return the pointer to the added var in the storage.
	 *
	 * @param var - the VariableObject to add
	 * @return - the pointer to the object in the array
	 * @throws DuplicateAssignmentException - if already exists
	 */
	public VariableObject addVar(VariableObject var) throws DuplicateAssignmentException {

		return varStore.addVar(var);

	}

	/**
	 * Gets the sourceFile.
	 *
	 * @return the sourceFile.
	 */
	public ArrayList<String> getSrc() {
		return sourceFile;
	}


	/**
	 * Gets the LinkedList of the scopes composed.
	 *
	 * @return LinkedList of the scopes composed.
	 */
	public LinkedList<Scope> getAllChildren() {
		return children;
	}

	/**
	 * Gets the LinkedList of the Method scopes composed.
	 *
	 * @return LinkedList of the Method scopes composed.
	 */
	public LinkedList<Method> getAllMethods() {
		return methods;
	}

	/**
	 * Gets parent.
	 *
	 * @return Value of parent.
	 */
	public Scope getParent() {
		return parent;
	}


}

