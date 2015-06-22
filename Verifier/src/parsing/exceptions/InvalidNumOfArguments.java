package parsing.exceptions;

/**
 * Thrown if invalid number of arguments were given to a method call.
 */
public class InvalidNumOfArguments extends ParsingException {
	public InvalidNumOfArguments() {
		super("Arguments dont match method signature");
	}
}
