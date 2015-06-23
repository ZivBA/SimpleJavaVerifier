package parsing;

import java.util.regex.Pattern;

/**
 * Storage for regex strings and patterns that are used in the SimpleJavaVerifier.
 */
public class RegexDepot {

	private static final String VAR_TYPES = "int|double|String|boolean|char";
	private static final String CONDITION_TYPE = "if|while";
	private static final String RESERVED_WORDS = VAR_TYPES+"|void|final|"+CONDITION_TYPE;
	public static final String VARIABLE_NAME = " ?(?!"+RESERVED_WORDS+")[a-zA-Z]+[\\w]*|[_][\\w]+";
	public static final String VARIABLE_ASSIGNMENT = "(" + VARIABLE_NAME + ") ?= ?(.+)";
	public static final String VALID_LINES = " *return *;|void ([a-zA-Z]+[\\w]*) ?\\((.*)\\) ?|}";
	public static final String VALID_BOOL_TYPES = "boolean|double|int";
	public static final String ARG_DELIM = " *, *";

	public static final String CONDITION_DELIM = "\\|\\||&&";
	public static final Pattern VARIABLE_PATTERN = Pattern.compile(VARIABLE_NAME);
	public static final Pattern VARIABLE_DECLARATION_PATTERN =
			Pattern.compile("(final )?("+VAR_TYPES+") ([^;]+);?");
	public static final Pattern VARIABLE_ASSIGNEMENT_PATTERN = Pattern.compile(VARIABLE_ASSIGNMENT);
	public static final Pattern METHOD_CALL_PATTERN = Pattern.compile("([a-zA-Z]+[\\w]*) ?\\((.*)\\) ?;");
	public static final Pattern CONDITION_PATTERN =
			Pattern.compile("("+CONDITION_TYPE+") ?\\((.+)\\) ?\\{?");
	public static final Pattern METHOD_PATTERN =
			Pattern.compile("void (?!"+RESERVED_WORDS+")([a-zA-Z]+[\\w]*) ?\\((.*)\\) ?\\{");

}
