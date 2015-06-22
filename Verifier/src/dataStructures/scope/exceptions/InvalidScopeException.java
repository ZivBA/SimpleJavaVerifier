package dataStructures.scope.exceptions;

/**
 * Invalid syntax at one of the lines in the scope.
 */
public class InvalidScopeException extends ScopeException {

	public InvalidScopeException(String s) {
		super("tried to create scope from the line: " + s + "\n but syntax is wrong");
	}
}
