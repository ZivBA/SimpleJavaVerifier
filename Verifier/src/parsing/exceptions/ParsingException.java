package parsing.exceptions;

/**
 * Parsing exception is the parent exception class for all parsing type exceptions.
 */
public class ParsingException extends Throwable {
	public ParsingException(String s) {
		super(s);
	}
}
