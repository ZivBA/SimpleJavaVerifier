package dataStructures.scope.exceptions;

/**
 * Missing or invalid return statement
 */
public class InvalidReturnException extends ScopeException {
	public InvalidReturnException() {
		super("Invalid return at end of method");
	}
}
