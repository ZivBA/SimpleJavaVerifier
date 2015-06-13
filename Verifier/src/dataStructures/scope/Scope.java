package dataStructures.scope;

import dataStructures.vars.VariableObject;
import dataStructures.vars.VariableStorage;
import parsing.exceptions.DuplicateAssignmentException;

import java.lang.Enum;
import java.util.ArrayList;

/**
 * Created by rooty on 6/11/15.
 */
public class Scope {

	public Enum Types{ // TODO get enums down, maybe get the name from the parser?
		METHOD,
		WHILE,
		IF;


	}

	// Data Members

	private final VariableStorage varStore = new VariableStorage();
	private Scope parent;
	private ArrayList<Scope> children;
	private String type;
	private String conditions;

	// Constructor
	//TODO - create constructor for Scope. not sure yet how to handle this.

	public Scope(Scope parent, String type){ // Constructor for method w/o parameters
		// TODO Check for legal types (void method, if, while)
		this.type = type;
		this.parent = parent;
	}

	public Scope(Scope parent, String type, ArrayList<VariableObject var> parameters){
		// check for legal type as above
		this.type = type;
		this.parent = parent;
		// add parameters vars to the store if scope is method
		if (type.equals(Types.METHOD)) {
			for (VariableObject var : paramaters) {
				addVar(var);
			}
			// TODO is there anything special for a scope of type WHILE/IF?
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
		VariableObject tempVar = varStore.getVar(var.getName());
		if (tempVar != null) {
			throw new parsing.exceptions.DuplicateAssignmentException(var, tempVar);
		} else {
			return varStore.addVar(var);
		}
	}

}

