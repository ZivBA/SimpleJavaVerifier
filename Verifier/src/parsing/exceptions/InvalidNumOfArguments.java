package parsing.exceptions;

/**
 * Created by user on 6/22/2015.
 */
public class InvalidNumOfArguments extends ParsingException {
	public InvalidNumOfArguments() {
		super("Arguments dont match method signature");
	}
}
