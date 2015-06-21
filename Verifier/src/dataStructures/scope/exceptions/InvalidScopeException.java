package dataStructures.scope.exceptions;

/**
 * Created by rooty on 6/14/15.
 */
public class InvalidScopeException extends ScopeException {

	public InvalidScopeException(String s) {
		 super("tried to create scope from the line: " + s + "\n but syntax is wrong");
	}
}
