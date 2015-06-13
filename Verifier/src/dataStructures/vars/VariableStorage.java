package dataStructures.vars;

import java.util.HashMap;

/**
 * Created by rooty on 6/11/15.
 */
public class VariableStorage {

	// Data Members

	private HashMap<String, VariableObject> varStore;

	public VariableStorage() {
		HashMap<String, VariableObject> varStore = new HashMap<>();
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
	public VariableObject addVar(VariableObject var) {
		// TODO if trying to add a variable and the name already exists throw exception
		if (varStore.containsKey(var.getName())) {
			return varStore.get(var.getName());
		}

		return varStore.put(var.getName(), var);
	}

}
