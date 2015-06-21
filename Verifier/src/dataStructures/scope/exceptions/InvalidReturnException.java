package dataStructures.scope.exceptions;

/**
 * Created by user on 6/21/2015.
 */
public class InvalidReturnException extends ScopeException {
	public InvalidReturnException(){
		super("Invalid return at end of method");
	}
}
