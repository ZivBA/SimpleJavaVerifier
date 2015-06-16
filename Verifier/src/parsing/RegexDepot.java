package parsing;

import java.util.regex.Pattern;

/**
 * Created by rooty on 6/14/15.
 */
public class RegexDepot {

	public final static String VAR_NAME = " ?[a-zA-Z]+[\\w]*|[_][\\w]+"; // TODO why is there a space here in the beginning?
	public final static String VAR_VALUE = "(" + VAR_NAME + ") ?= ?(.+)";

	public final static Pattern SCOPE_PATTERN = Pattern.compile("void|if|while");
	public final static Pattern VAR_PATTERN = Pattern.compile(VAR_NAME);
	public final static Pattern METHOD_DECLERATION =
			Pattern.compile("(final )?(int|double|String|boolean|char) (" + VAR_NAME + ")"); // TODO did you mean to put scope pattern in here?
	public final static Pattern VAR_DECLERATION =
			Pattern.compile("(final )?(int|double|String|boolean|char) ([^;]+);");
	public final static Pattern VAR_ASSIGN = Pattern.compile(VAR_VALUE + " ?;");
	public final static Pattern METHOD_CALL_PATTERN = Pattern.compile("([a-zA-Z]+[\\w]*) ?\\((.*)\\) ?;");
	public final static Pattern RETURN_PATTERN = Pattern.compile("return ?;");
	public final static Pattern CONDITION_PATTERN = Pattern.compile("(if|while) ?\\((.+)\\) ?\\{");
	public final static Pattern METHOD_PATTERN = Pattern.compile("void ([a-zA-Z]+[\\w]*) ?\\((.*)\\) ?\\{");
}
