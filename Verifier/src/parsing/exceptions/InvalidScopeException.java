package parsing.exceptions;

/**
 * Created by rooty on 6/14/15.
 */
public class InvalidScopeException extends Throwable {
	public String errorMsg;

	public InvalidScopeException(String s) {
		errorMsg = "tried to create scope from the line: " + s + "\n but syntax is wrong";
	}
}
