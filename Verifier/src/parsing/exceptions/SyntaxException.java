package parsing.exceptions;

/**
 * Syntax exception is thrown whenever an invalid syntax in recognized in the code.
 */
public class SyntaxException extends ParsingException {
	public SyntaxException(String line) {
		super("Invalid syntax at: \n"+line);
	}
}
