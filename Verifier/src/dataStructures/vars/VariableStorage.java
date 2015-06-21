package dataStructures.vars;

import dataStructures.vars.exceptions.DuplicateAssignmentException;

import java.util.HashMap;

/**
 * Created by rooty on 6/11/15.
 */
public class VariableStorage {

	// Data Members

	private HashMap<String, VariableObject> varStore;

	public VariableStorage() {
		varStore = new HashMap<>();
	}

	// Methods

	/**
	 * query the DB for a variable name. returns NULL if not present.
	 *
	 * @param name
	 * @return
	 */
	public VariableObject getVar(String name) {
		return varStore.get(name);
	}

	/**
	 * adds the var to the storage if it doesnt exist. returns pre-existing var with same name if it does.
	 *
	 * @param var
	 * @return
	 */
	public VariableObject addVar(VariableObject var) throws DuplicateAssignmentException {
		// TODO if trying to add a variable and the name already exists throw exception
		// TODO if the value is a variable name then check if it is initialized
		if (varStore.containsKey(var.getName())) {
			throw new DuplicateAssignmentException(var);
		} // else if var.getValue != initialized value throw new exception

		return varStore.put(var.getName(), var);
	}


}
