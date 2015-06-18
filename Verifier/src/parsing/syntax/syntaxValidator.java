package parsing.syntax;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rooty on 6/11/15.
 */
public class syntaxValidator {


	// regex expressions:
	// TODO should these have modifiers? Should we Enum the strings?
	protected Pattern scopeOpen = Pattern.compile("\\{");
	protected Pattern scopeClose = Pattern.compile("\\}");
	protected Pattern bracketOpen = Pattern.compile("\\("); // TODO check that these are legal
	protected Pattern bracketClose = Pattern.compile("\\)");
	protected Pattern semicolonEnd = Pattern.compile(";");


	public static String syntaxValidator(Scanner sourceFile) throws FileNotFoundException {

		String parsedSource = cleanFile(sourceFile);

		// TODO do acutal validation

		return parsedSource;

	}

	private static String cleanFile(Scanner sourceFile) throws FileNotFoundException {
		//return a string representation of the scanned file
		String stringFile = sourceFile.useDelimiter("\\A").next();
		//delete all the legal comment from the file
		stringFile = stringFile.replaceAll("(\\A|\\n)//.*", "");
		//replace all the white space except the line skipping
		stringFile = stringFile.replaceAll("[^\\S\n]+", " ");
		//replace one or more line skipping by one line skipping
		stringFile = stringFile.replaceAll(" ?\n+ ?", "\n");
		stringFile = stringFile.replaceAll("\n+", "\n");
		//clean all the white space at the beginning and the end of the file
		stringFile = stringFile.trim();

		return stringFile;
	}

	private void searchForMissingSyntax() {
   /* add stack for curlybrackets, brackets
   Go over each line, for each line, if meet void/if/while, search for brackets, after, search for curly
   opener, remember to search for curly closer. if line dosent end with any curly, search for ;.
    */
		Stack<String> brackets = new Stack<>();
		Stack<String> curlyBrackets = new Stack<>();

		String currentLine;
		while (sourceFile.hasNextLine()) {
			currentLine = sourceFile.nextLine();
			Matcher matchOpenBracket = bracketOpen.matcher(currentLine);
			if (matchOpenBracket.matches()) {

			}
		}
	}

}