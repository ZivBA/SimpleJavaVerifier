package dataStructures.scope.exceptions;

/**
 * Created by rooty on 6/21/15.
 */
public class invalidMethodException extends ScopeException {
	public invalidMethodException(String methodName) {
		super("The method: " + methodName + " was not properly called or wasn't declared");
	}
}
