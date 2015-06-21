package dataStructures.vars.exceptions;

/**
 * Created by user on 6/21/2015.
 */
public class IllegalAssignmentException extends VariableException {

	public IllegalAssignmentException(String var) {
		super("assignment error for: " + var);
	}
}
