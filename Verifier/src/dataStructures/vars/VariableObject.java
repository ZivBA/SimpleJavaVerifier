/**
 * Created by rooty on 6/11/15.
 */
package dataStructures.vars;

public class VariableObject {

	/**
	 * enumerator class nested in VariableObject class.
	 * Holds the legal types and patterns of values that are allowed to be made in VariableObject.
	 */
	public enum VarTypeAndValue {
		INT("int", "-?[0-9]+"),
		DOUBLE("double", "-?[0-9]+(\\.[0-9]+)?"),
		STRING("String", "\".*\""),
		CHAR("char", "\'.\'"),
		BOOLEAN("boolean", "((true|false)|(-?[0-9]+(\\.[0-9]+)?))");

		private String type;
		private String pattern;

		//Constructor
		VarTypeAndValue(String type, String pattern){
			this.type = type;
			this.pattern = pattern;
		}

		/**
		 * Gets the type.
		 * @return the type of the value
		 */
		public String getType() {
			return type;
		}

		/**
		 * Gets the pattern.
		 * @return the pattern of the value
		 */
		public String getPattern() {
			return pattern;
		}
	}


	private final String name;
	private final String type;
	private String value;
	private boolean isFinal = false; // will change if given in constructor


	/**
	 * Constructor one - gets all params for creation of initialized variable.
	 *
	 * @param name
	 * @param type
	 * @param value
	 * @param isFinal
	 */
	public VariableObject(String name, String type, String value, Boolean isFinal)
			throws IllegalAssignmentException {
		this.name = name;
		this.type = type;
		this.value = value;
		this.isFinal = isFinal;

		if (!checkLegalValue()){
			throw new IllegalAssignmentException();
		}
	}

	/**
	 * Constructor two - gets all but Value param - creates an uninitialized variable.
	 *
	 * @param name
	 * @param type
	 */
	public VariableObject(String name, String type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * Checks that the value string given to the constructor matches the legal patterns of the known types.
	 * @return true if the pattern of the given value matches a known types value pattern.
	 */
	private boolean checkLegalValue(){
		for (VarTypeAndValue item : VarTypeAndValue.values()){
			if (this.type == item.getType()){ // validate that the type will match the pattern
				if (this.value.matches(item.getPattern())){
					return true;
				} else return false;
			}
		}
		return false;
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
	public void setValue(String value) {
		this.value = value;
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
	 * Get if variable is final
	 *
	 * @return true if final, else false.
	 */
	public boolean isFinal() {
		return isFinal;
	}

	//##################################################################################


	public boolean equals(VariableObject obj) {
		return (this.getName().equals(obj.getName()));
	}
}