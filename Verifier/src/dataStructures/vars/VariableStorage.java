package dataStructures.vars;

import dataStructures.vars.exceptions.DuplicateAssignmentException;

import java.util.HashMap;

/**
 * Data type that holds all the initialized variableObjects in the scope that composes it.
 * implemented using a HashMap to allow for quick querying according to the Variable name alone.
 */
public class VariableStorage {

	// Data Members

	private final HashMap<String, VariableObject> varStore;

	/**
	 * default constructor, simply creates a new hashmap for the object.
	 */
	public VariableStorage() {
		varStore = new HashMap<>();
	}

	// Methods

	/**
	 * query the DB for a variable name. returns NULL if not present.
	 *
	 * @param name the name of the variable to find
	 * @return 	the VarObj found or null
	 */
	public VariableObject getVar(String name) {
		return varStore.get(name);
	}

	/**
	 * adds the var to the storage if it doesnt exist. Throws exception if its already present.
	 *
	 * @param var the VarObj to add
	 * @return the reference to the VarObj in the storage array.
	 */
	public VariableObject addVar(VariableObject var) throws DuplicateAssignmentException {
		if (varStore.containsKey(var.getName())) {
			throw new DuplicateAssignmentException(var);
		}

		return varStore.put(var.getName(), var);
	}


}
