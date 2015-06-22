package dataStructures.scope.exceptions;

/**
 * inconsistency between method decleration (or no decleration at all) and method call
 */
public class invalidMethodException extends ScopeException {
	public invalidMethodException(String methodName) {
		super("The method: " + methodName + " was not properly called or wasn't declared");
	}
}
