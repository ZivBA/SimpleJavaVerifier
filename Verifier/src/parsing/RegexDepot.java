package parsing;

import java.util.regex.Pattern;

/**
 * Created by rooty on 6/14/15.
 */
public class RegexDepot {

	public static final String VARIABLE_NAME=" ?[a-zA-Z]+[\\w]*|[_][\\w]+";
	public static final String VARIABLE_ASSIGNEMENT = "(" + VARIABLE_NAME + ") ?= ?(.+)";

	public static final Pattern VARIABLE_PATTERN = Pattern.compile(VARIABLE_NAME);
	public static final Pattern VARIABLE_DECLARATION_PATTERN =
			Pattern.compile("(final )?(int|double|String|boolean|char) ([^;]+);");
	public static final Pattern VARIABLE_ASSIGNEMENT_PATTERN = Pattern.compile(VARIABLE_ASSIGNEMENT + " ?;");
	public static final Pattern METHOD_CALL_PATTERN = Pattern.compile("([a-zA-Z]+[\\w]*) ?\\((.*)\\) ?;");
	public static final Pattern RETURN_PATTERN = Pattern.compile("return ?;");
	public static final Pattern CONDITION_PATTERN = Pattern.compile("(if|while) ?\\((.+)\\) ?\\{");
	public static final Pattern METHOD_PATTERN= Pattern.compile("void ([a-zA-Z]+[\\w]*) ?\\((.*)\\) ?\\{");

}
