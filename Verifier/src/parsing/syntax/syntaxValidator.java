package parsing.syntax;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.Enum;

/**
 * Created by rooty on 6/11/15.
 */
public class syntaxValidator {


	// regex expressions:
	// TODO should these have modifiers? Should we Enum the strings?
	protected Pattern scopeOpen = Pattern.compile("{");
	protected Pattern scopeClose = Pattern.compile("}");
	protected Pattern parenthesesOpen = Pattern.compile("("); // TODO check that these are legal
	protected Pattern parenthesesClose = Pattern.compile(")");
	protected Pattern comment = Pattern.compile("//");
	protected Pattern lineEnd = Pattern.compile(";");
	protected Pattern assign = Pattern.compile("=");
	protected Pattern varType = Pattern.compile("int||double||boolean||String||char");
	protected Pattern scopeType = Pattern.compile("void || while || if");
	// varName may not start with digit, if starts with underscore, it must have at least another char
	//
	protected Pattern varName = Pattern.compile("");
	protected Pattern intValue = Pattern.compile("-?\\d+"); // TODO add other varNames to values?
	protected Pattern doubleValue = Pattern.compile("-?\\d+(\\.?\\d)"); // is this okay?
	protected Pattern booleanValue = Pattern.compile("true || false || \\d+");
	//protected Pattern stringValue = Pattern.compile("\\"\\S")

	/*
	Patterns to add:
	final
	return
	methodName
	,
	AND OR operators
	 */

}
