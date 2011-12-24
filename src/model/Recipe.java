/**
 * 
 */
package model;

/**
 * @author Emil Sågfors
 */
public class Recipe {

	/**
	 * The name of the drink.
	 */
	private String name;
	/**
	 * The list of ingredients used in the drink.
	 */
	private IngredientList ingredients;
	/**
	 * The instructions for preparing the drink.
	 */
	private String[] instructions;
	/**
	 * Price of the product. Will not neccessarily be used in final product
	 * (dynamically calculated).
	 */
	private Double price;

	/**
	 * Creates a new recipe from the specified parameters. //TODO @param
	 */
	public Recipe() {
		this("");
	}

	public Recipe(String name) {
		this(name, 0);
	}

	public Recipe(String name, double price) {
		this.name = name;
		this.price = price;
	}

	/**
	 * @return the name of the drink
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the ingredients used in the drink
	 */
	public IngredientList getIngredientList() {
		return this.ingredients;
	}

	/**
	 * @return the instructions
	 */
	public String[] getInstructions() {
		return this.instructions;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString() */
	@Override
	public String toString() {
		// TODO use a listcellrenderer instead
		return this.getName();
	}

	/**
	 * @return
	 */
	public Double getPrice() {
		// TODO Auto-generated method stub
		return this.price;
	}
}
