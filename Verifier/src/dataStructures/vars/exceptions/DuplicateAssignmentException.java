package dataStructures.vars.exceptions;

import dataStructures.vars.VariableObject;

/**
 * if an attempt is made to add a variable with the same name to this scope..
 */
public class DuplicateAssignmentException extends VariableException {

	public DuplicateAssignmentException(VariableObject var) {
		super("Tried to assign " + var.getType() + " to variable " + var.getName() + "but it already " +
				"exists in this scope");
	}
}
