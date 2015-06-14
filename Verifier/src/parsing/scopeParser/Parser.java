/**
 * Created by rooty on 6/11/15.
 */
package parsing.scopeParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Parser {

	public Parser(ArrayList<String> scope) throws IOException {


	}

	public static Scanner cleanFile(File sourceFile) throws FileNotFoundException {
		Scanner tempScan = new Scanner(sourceFile);
		//return a string representation of the scanned file
		String stringFile = tempScan.useDelimiter("\\A").next();
		//delte all the legal comment from the file
		stringFile = stringFile.replaceAll("(\\A|\\n)//.*", "");
		//replace all the white space except the line skipping
		stringFile = stringFile.replaceAll("[^\\S\n]+", " ");
		//replace one or more line skipping by one line skipping
		stringFile = stringFile.replaceAll(" ?\n+ ?", "\n");
		stringFile = stringFile.replaceAll("\n+", "\n");
		//clean all the white space at the beginning and the end of the file
		stringFile = stringFile.trim();

		tempScan.close();
		return new Scanner(stringFile);
	}
}
