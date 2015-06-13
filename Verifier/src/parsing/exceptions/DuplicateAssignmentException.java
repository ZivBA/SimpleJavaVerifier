package parsing.exceptions;

import dataStructures.vars.VariableObject;

/**
 * Created by rooty on 6/13/15.
 */
public class DuplicateAssignmentException extends Throwable {
	public String errorMsg;

	public DuplicateAssignmentException(VariableObject var, VariableObject tempVar) {
		errorMsg = "Tried to assign " + var.getType() + " to variable " + var.getName() + "but it already " +
				"exists " +
				"in this scope as: " + tempVar.getType();
	}
}
