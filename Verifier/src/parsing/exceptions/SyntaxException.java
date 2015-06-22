package parsing.exceptions;

/**
 * Syntax exception is thrown whenever an invalid syntax in recognized in the code.
 */
public class SyntaxException extends ParsingException {
	public SyntaxException() {
		super("Invalid syntax");
	}
}
