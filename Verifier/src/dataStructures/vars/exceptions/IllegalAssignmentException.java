package dataStructures.vars.exceptions;

/**
 * if an improper value is assigned to a variable, or if a reference variable is used without being
 * initialized first..
 */
public class IllegalAssignmentException extends VariableException {

	public IllegalAssignmentException(String var) {
		super("assignment error for: " + var);
	}
}
