/**
 * Created by rooty on 6/11/15.
 */
package dataStructures.vars;

import dataStructures.vars.exceptions.IllegalAssignmentException;

/**
 * The VarObj class represents an sjava variable. it's a generalized (not Generic!) form of a variable,
 * with minimal API - just name, type, value and an isFinal flag for each variable. </br>
 * Variable values are stored as strings regardless of type, and an Enum of types and matching patterns is
 * stored for type checking.
 */
public class VariableObject {

	// data members
	private final String name;
	private final String type;
	private String value;
	private boolean isFinal = false; // will change if given in constructor

	/**
	 * Constructor one - gets all params for creation of initialized variable.
	 *
	 * @param name 	- Var name
	 * @param type	- Type of var (int|bool|char|string|double)
	 * @param value - String representation of the Var value
	 * @param isFinal - boolean modifier - isFinal
	 */
	public VariableObject(String name, String type, String value, boolean isFinal)
			throws IllegalAssignmentException {
		this.name = name;
		this.type = type;
		this.value = value;
		this.isFinal = isFinal;

		// checks validity of value Vs. type - if an improper assignment is detected, throws an exception.
		if (checkForIllegalValue()) {
			throw new IllegalAssignmentException(name);
		}
	}


	/**
	 * Constructor two - gets all but Value param - creates an uninitialized variable.
	 *
	 * @param name 	- Var name
	 * @param type	- Type of var (int|bool|char|string|double)
	 */
	public VariableObject(String name, String type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * Checks that the value string given to the constructor matches the legal patterns of the known types.
	 *
	 * @return true if the pattern of the given value matches a known types value pattern.
	 */
	private boolean checkForIllegalValue() {
		if (this.value == null) {
			return false;
		}

		for (VarTypeAndValue item : VarTypeAndValue.values()) {
			if (this.type.equals(item.getType())) {	 // look for the matching type in the Enum VarTypeAndValue

				// validate that the type will match the pattern
				return !this.value.matches(item.getPattern());
			}
		}
		return true;
	}

	//################ getters for all fields + setter for new value ###################


	/**
	 * Gets value.
	 *
	 * @return Value of value.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets new value.
	 *
	 * @param value New value of value.
	 */
	public void setValue(String value) throws IllegalAssignmentException {

		this.value = value;


		if (checkForIllegalValue()) {
			throw new IllegalAssignmentException(name);
		}
	}

	/**
	 * Gets type.
	 *
	 * @return Value of type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * Gets name.
	 *
	 * @return Value of name.
	 */

	public String getName() {
		return name;
	}

	/**
	 * Checks if variable is final
	 *
	 * @return true if final, else false.
	 */
	public boolean isFinal() {
		return isFinal;
	}

	/**
	 * overrides the Object default "contains" method to allow for custom comparison of VariableObjects.
	 * this is necessary to allow for searching a LinkedList or ArrayList according to a variable name,
	 * disregarding the value and type
	 * @param obj	- the object to match
	 * @return		- true if both objects have the same name.
	 */
	public boolean equals(Object obj) {
		return (((VariableObject)obj).getName().equals(name));
	}

	/**
	 * sets the Variable as final or unsets as necessary.
	 * @param isFinal
	 */
	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}


	//##################################################################################


	/**
	 * enumerator class nested in VariableObject class.
	 * Holds the legal types and patterns of values that are allowed to be made in VariableObject.
	 */
	public enum VarTypeAndValue {
		INT("int", "-?[0-9]+ *;?"),
		DOUBLE("double", "-?[0-9]+(\\.[0-9]+)? *;?"),
		STRING("String", "\".*\" *;?"),
		CHAR("char", "\'.\' *;?"),
		BOOLEAN("boolean", "( *(true|false)|(-?[0-9]+(\\.[0-9]+)?)) *;?");

		private final String type;
		private final String pattern;

		// Mandatory Constructor
		VarTypeAndValue(String type, String pattern) {
			this.type = type;
			this.pattern = pattern;
		}

		/**
		 * Gets the type.
		 *
		 * @return the type of the value
		 */
		public String getType() {
			return type;
		}

		/**
		 * Gets the pattern.
		 *
		 * @return the pattern of the value
		 */
		public String getPattern() {
			return pattern;
		}
	}
}