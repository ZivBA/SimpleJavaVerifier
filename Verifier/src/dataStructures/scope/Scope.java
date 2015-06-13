package dataStructures.scope;

import dataStructures.vars.VariableObject;
import dataStructures.vars.VariableStorage;
import parsing.exceptions.DuplicateAssignmentException;

import java.util.ArrayList;

/**
 * Created by rooty on 6/11/15.
 */
public class Scope {
	private final VariableStorage varStore = new VariableStorage();
	private Scope parent;
	private ArrayList<Scope> children;
	private String type;
	private String conditions;

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
		VariableObject tempVar = varStore.getVar(var.getName());
		if (tempVar != null) {
			throw new parsing.exceptions.DuplicateAssignmentException(var, tempVar);
		} else {
			return varStore.addVar(var);
		}
	}

}

