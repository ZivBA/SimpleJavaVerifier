package dataStructures.scope;

import dataStructures.vars.VariableStorage;
import parsing.RegexDepot;
import parsing.exceptions.InvalidScopeException;

import java.util.ArrayList;
import java.util.regex.Matcher;

/**
 * Created by rooty on 6/21/15.
 */
public class Method extends Scope {
	private String name = null;
	private static final String TYPE = "Method";

	private String argString = "";
	private VariableStorage arguments = new VariableStorage();

	public Method(ArrayList<String> sourceFile, Scope parent) throws InvalidScopeException {
		this.sourceFile = sourceFile;
		this.parent = parent;

		parseParams();
		recurScopeBuilder();
	}

	private void parseParams() throws InvalidScopeException {

		String firstLine = sourceFile.get(0);
		Matcher methodMatch = RegexDepot.METHOD_PATTERN.matcher(firstLine);
		methodMatch.find();
		name = methodMatch.group(1);
		argString = methodMatch.group(2);
		sourceFile.remove(0);


	}


	public String getName() {
		return name;
	}


	public String getArgString() {
		return argString;
	}

	public VariableStorage getArguments() {
		return arguments;
	}


}
