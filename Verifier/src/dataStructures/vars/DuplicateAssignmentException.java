package dataStructures.vars;

import dataStructures.vars.VariableObject;

/**
 * Created by rooty on 6/13/15.
 */
public class DuplicateAssignmentException extends VariableException {

	public DuplicateAssignmentException(VariableObject var) {
		super("Tried to assign " + var.getType() + " to variable " + var.getName() + "but it already " +
				"exists in this scope");
	}
}
