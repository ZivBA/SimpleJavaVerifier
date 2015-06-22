package dataStructures.scope.exceptions;

/**
 * an invalid condition was encountered within a scope
 */
public class InvalidConditionsException extends ScopeException {
	public InvalidConditionsException() {
		super("Invalid condition in scope");
	}
}
