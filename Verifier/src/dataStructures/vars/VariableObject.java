/**
 * Created by rooty on 6/11/15.
 */
package dataStructures.vars;

public class VariableObject {
	private final String name;
	private final String type;
	private String value;


	/**
	 * Constructor one - gets all params for creation of initialized variable.
	 *
	 * @param name
	 * @param type
	 * @param value
	 */
	public VariableObject(String name, String type, String value) {
		this.name = name;
		this.type = type;
		this.value = value;
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

	//##################################################################################

}